package com.mobiquityinc.packer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Packet;
import com.mobiquityinc.model.PacketItem;

public class Packer {

	private static Pattern fileExtnPtrn = Pattern.compile("([^\\s]+(\\.(?i)(txt))$)");

	private Packer() {
	}

	public static String pack(String filePath) throws APIException {
		validateFileExtn(filePath);

		List<String> list = new ArrayList<>();

		String result = "";
		try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {

			list = br.lines().collect(Collectors.toList());

			List<Packet> packets = getPacketsFromFileInput(list);

			for (Packet packet : packets) {

				List<PacketItem> packetsInWeightLimit = packet.getItems().stream()
						.filter(p -> p.getWeight() <= packet.getWeightLimit()).collect(Collectors.toList());

				List<List<PacketItem>> combinations = createCombinations(packetsInWeightLimit);
				if (combinations.size() == 0) {
					result += "-\n";
				} else {
					List<PacketItem> bestCombination = getBestPackage(combinations, packet.getWeightLimit());
					result += printOutput(bestCombination) + "\n";
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static List<Packet> getPacketsFromFileInput(List<String> list) {
		List<Packet> packets = new ArrayList<>();

		for (String line : list) {

			List<PacketItem> packageItems = new ArrayList<>();

			String[] tokens = line.split(":");

			String packetsString = tokens[1].trim();
			String[] packetsTokens = packetsString.split(" ");

			for (int i = 0; i < packetsTokens.length; i++) {

				String packetItemsString = packetsTokens[i].trim();
				String[] packetItemsTokens = packetItemsString.split(",");

				packageItems.add(new PacketItem(Integer.parseInt(packetItemsTokens[0].substring(1)),
						Double.parseDouble(packetItemsTokens[1]),
						Double.parseDouble(packetItemsTokens[2].substring(1, packetItemsTokens[2].length() - 1))));
			}

			packets.add(new Packet(Integer.parseInt(tokens[0].trim()), packageItems));
		}

		return packets;
	}

	private static List<List<PacketItem>> createCombinations(List<PacketItem> packetsInWeightLimit) {
		List<List<PacketItem>> combinations = new ArrayList<List<PacketItem>>();
		for (int x = 0; x < packetsInWeightLimit.size(); x++) {
			PacketItem currentItem = packetsInWeightLimit.get(x);
			int combinationSize = combinations.size();
			for (int y = 0; y < combinationSize; y++) {
				List<PacketItem> combination = combinations.get(y);
				List<PacketItem> newCombination = new ArrayList<PacketItem>(combination);
				newCombination.add(currentItem);
				combinations.add(newCombination);
			}
			ArrayList<PacketItem> current = new ArrayList<PacketItem>();
			current.add(currentItem);
			combinations.add(current);
		}

		return combinations;
	}

	private static double getWeight(List<PacketItem> items) {
		double total = 0;
		for (PacketItem i : items) {
			total += i.getWeight();
		}
		return total;
	}

	private static double getPrice(List<PacketItem> items) {
		double total = 0;
		for (PacketItem i : items) {
			total += i.getCost();
		}
		return total;
	}

	private static List<PacketItem> getBestPackage(List<List<PacketItem>> combinations, Integer limit) {
		List<PacketItem> bestCombination = new ArrayList<PacketItem>();
		double bestCost = 0;
		double bestWeight = 100; // max weight is 100
		for (List<PacketItem> combination : combinations) {
			double combinationWeight = getWeight(combination);
			if (combinationWeight > limit) {
				continue;
			} else {
				double combinationPrice = getPrice(combination);
				if (combinationPrice > bestCost) {
					bestCost = combinationPrice;
					bestCombination = combination;
					bestWeight = combinationWeight;
				} else if (combinationPrice == bestCost) { // use lightest weight
					if (combinationWeight < bestWeight) {
						bestCost = combinationPrice;
						bestCombination = combination;
						bestWeight = combinationWeight;
					}
				}
			}
		}
		return bestCombination;
	}

	private static String printOutput(List<PacketItem> items) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (PacketItem i : items) {
			if (isFirst) {
				sb.append(i.getIndexNumber());
				isFirst = false;
			} else {
				sb.append("," + i.getIndexNumber());
			}
		}
		return sb.toString();
	}

	private static boolean validateFileExtn(String fileName) throws APIException {

		Matcher mtch = fileExtnPtrn.matcher(fileName);
		if (mtch.matches()) {
			return true;
		}
		throw new APIException("Invalid Filename");
	}

}
