package spell;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import spell.ITrie.INode;

public class SpellCorrector implements ISpellCorrector {

	private Trie trie;
	
	public SpellCorrector() {
		trie = new Trie();
	}
	
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		
		Scanner sc = new Scanner(new BufferedInputStream(new FileInputStream(dictionaryFileName)));
		while(sc.hasNext()) {
			
			String next = sc.next().toLowerCase();
			trie.add(next);
		}
	}

	@Override
	public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
		
		//String trieString = trie.toString();
		inputWord = inputWord.toLowerCase();
		if(trie.find(inputWord) != null)
			return inputWord;
		
		ArrayList<String> resultOne = trie.transformation(inputWord);
		String bestWord = "";
		int maxFrequencyCount = 0;
		
		Collections.sort(resultOne);
		for(String s : resultOne) {
			
			if(trie.find(s) != null) {
			INode nodeTemp = trie.find(s);
			
			if(nodeTemp.getValue() > maxFrequencyCount) {
				bestWord = s;
				maxFrequencyCount = nodeTemp.getValue();
			}
			}
		}
		
		if(!bestWord.equals("") && maxFrequencyCount != 0)
			return bestWord;
		
		ArrayList<String> resultTwo = new ArrayList<>();
		
		for(String s : resultOne) {
			resultTwo.addAll(trie.transformation(s));
		}
		
		Collections.sort(resultTwo);
		for(String s : resultTwo) {
			
			if(trie.find(s) != null) {
			INode nodeTemp = trie.find(s);
			
			if(nodeTemp.getValue() > maxFrequencyCount) {
				bestWord = s;
				maxFrequencyCount = nodeTemp.getValue();
			}
			}
		}
		
		if(!bestWord.equals("") && maxFrequencyCount != 0)
			return bestWord;
		
		throw new NoSimilarWordFoundException();
	}

}
