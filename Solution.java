
public class Solution {

    public String mostCommonWord(String paragraph, String[] bannedWords) {
        Trie trie = new Trie();
        for (String word : bannedWords) {
            trie.addBannedWord(word);
        }
        trie.extractAndAddAllWordsFromMixedStream(paragraph);
        return trie.nonBannedWordWithMaxFrequency;
    }
}

class Trie {

    private class TrieNode {

        private static final int ALPHABET_SIZE = 26;
        TrieNode[] branches = new TrieNode[ALPHABET_SIZE];
        boolean isBanned;
        int frequency;
    }

    private static final char NULL_CHAR = '\u0000';
    private final TrieNode root = new TrieNode();
    private int maxFrequencyNonBannedWord;
    public String nonBannedWordWithMaxFrequency;

    public void addBannedWord(String word) {
        TrieNode node = root;
        for (char current : word.toCharArray()) {
            int index = Character.toLowerCase(current) - 'a';
            if (node.branches[index] == null) {
                node.branches[index] = new TrieNode();
            }
            node = node.branches[index];
        }
        node.isBanned = true;
    }

    public void extractAndAddAllWordsFromMixedStream(String stream) {
        int index = 0;

        while (index < stream.length()) {

            TrieNode node = root;
            int startIndex = index;
            char currentChar = stream.charAt(index);

            while (Character.isLetter(currentChar)) {
                int i = Character.toLowerCase(currentChar) - 'a';
                if (node.branches[i] == null) {
                    node.branches[i] = new TrieNode();
                }
                node = node.branches[i];
                currentChar = (++index < stream.length()) ? stream.charAt(index) : NULL_CHAR;
            }

            if (startIndex == index) {
                ++index;
                continue;
            }

            ++node.frequency;
            if (!node.isBanned && maxFrequencyNonBannedWord < node.frequency) {
                maxFrequencyNonBannedWord = node.frequency;
                nonBannedWordWithMaxFrequency = stream.substring(startIndex, index).toLowerCase();
            }
        }
    }
}
