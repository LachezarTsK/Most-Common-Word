
using System;

public class Solution
{
    public string MostCommonWord(string paragraph, string[] bannedWords)
    {
        Trie trie = new Trie();
        foreach (string word in bannedWords)
        {
            trie.addBannedWord(word);
        }
        trie.extractAndAddAllWordsFromMixedStream(paragraph);
        return trie.nonBannedWordWithMaxFrequency;
    }
}

class Trie
{
    private class TrieNode
    {
        private static readonly int ALPHABET_SIZE = 26;
        public readonly TrieNode[] branches = new TrieNode[ALPHABET_SIZE];
        public bool isBanned;
        public int frequency;
    }

    private static readonly char NULL_CHAR = '\u0000';
    private readonly TrieNode root = new TrieNode();
    private int maxFrequencyNonBannedWord;
    public String? nonBannedWordWithMaxFrequency;

    public void addBannedWord(String word)
    {
        TrieNode node = root;
        foreach (char currentChar in word)
        {
            int index = char.ToLower(currentChar) - 'a';
            if (node.branches[index] == null)
            {
                node.branches[index] = new TrieNode();
            }
            node = node.branches[index];
        }
        node.isBanned = true;
    }

    public void extractAndAddAllWordsFromMixedStream(String stream)
    {
        int index = 0;

        while (index < stream.Length)
        {
            TrieNode node = root;
            int startIndex = index;
            char currentChar = stream[index];

            while (char.IsLetter(currentChar))
            {
                int i = char.ToLower(currentChar) - 'a';
                if (node.branches[i] == null)
                {
                    node.branches[i] = new TrieNode();
                }
                node = node.branches[i];
                currentChar = (++index < stream.Length) ? stream[index] : NULL_CHAR;
            }

            if (startIndex == index)
            {
                ++index;
                continue;
            }

            ++node.frequency;
            if (!node.isBanned && maxFrequencyNonBannedWord < node.frequency)
            {
                maxFrequencyNonBannedWord = node.frequency;
                nonBannedWordWithMaxFrequency = stream.Substring(startIndex, index - startIndex).ToLower();
            }
        }
    }
}
