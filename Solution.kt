
class Solution {
    fun mostCommonWord(paragraph: String, bannedWords: Array<String>): String {
        val trie = Trie();
        for (word in bannedWords) {
            trie.addBannedWord(word);
        }
        trie.extractAndAddAllWordsFromMixedStream(paragraph);
        return trie.nonBannedWordWithMaxFrequency;
    }
}

class Trie {

    class TrieNode {

        companion object {
            const val ALPHABET_SIZE = 26;
        }

        val branches = arrayOfNulls<TrieNode>(ALPHABET_SIZE);
        var isBanned: Boolean = false;
        var frequency = 0;
    }

    companion object {
        const val NULL_CHAR: Char = '\u0000';
    }

    private val root: TrieNode = TrieNode();
    private var maxFrequencyNonBannedWord = 0;
    var nonBannedWordWithMaxFrequency: String = "";

    fun addBannedWord(word: String) {
        var node: TrieNode = root;
        for (currentChar in word) {
            val index = Character.toLowerCase(currentChar) - 'a';
            if (node.branches[index] == null) {
                node.branches[index] = TrieNode();
            }
            node = node.branches[index]!!;
        }
        node.isBanned = true;

    }

    fun extractAndAddAllWordsFromMixedStream(stream: String) {
        var index = 0;

        while (index < stream.length) {

            var node: TrieNode = root;
            val startIndex = index;
            var currentChar: Char = stream[index];

            while (currentChar.isLetter()) {
                val i: Int = Character.toLowerCase(currentChar) - 'a'
                if (node.branches[i] == null) {
                    node.branches[i] = TrieNode();
                }
                node = node.branches[i]!!;
                currentChar = if (++index < stream.length) stream[index] else NULL_CHAR;
            }

            if (startIndex == index) {
                ++index;
                continue;
            }

            ++node.frequency;
            if (!node.isBanned && maxFrequencyNonBannedWord < node.frequency) {
                maxFrequencyNonBannedWord = node.frequency;
                nonBannedWordWithMaxFrequency = stream.substring(startIndex, index).lowercase();
            }
        }
    }
}
