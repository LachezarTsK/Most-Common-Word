
/**
 * @param {string} paragraph
 * @param {string[]} bannedWords
 * @return {string}
 */
var mostCommonWord = function (paragraph, bannedWords) {
    const trie = new Trie();
    for (let word of bannedWords) {
        trie.addBannedWord(word);
    }
    trie.extractAndAddAllWordsFromMixedStream(paragraph);
    return trie.nonBannedWordWithMaxFrequency;
};

class TrieNode {

    static ALPHABET_SIZE = 26;
    branches = new Array(TrieNode.ALPHABET_SIZE).fill(null);
    isBanned = false;
    frequency = 0;
}

class Trie {

    static NULL_CHAR = '\u0000';
    root = new TrieNode();
    maxFrequencyNonBannedWord = 0;
    nonBannedWordWithMaxFrequency = "";

    /**
     * @param {string} word
     * @return {void}
     */
    addBannedWord(word) {
        let node = this.root;
        for (let currentChar of word) {
            let index = this.codePoint(currentChar.toLowerCase()) - this.codePoint('a');
            if (node.branches[index] === null) {
                node.branches[index] = new TrieNode();
            }
            node = node.branches[index];
        }
        node.isBanned = true;
    }

    /**
     * @param {string} stream
     * @return {void}
     */
    extractAndAddAllWordsFromMixedStream(stream) {
        let index = 0;

        while (index < stream.length) {

            let node = this.root;
            let startIndex = index;
            let currentChar = stream.charAt(index);

            while (this.isLetter(currentChar)) {
                const i = this.codePoint(currentChar.toLowerCase()) - this.codePoint('a');
                if (node.branches[i] === null) {
                    node.branches[i] = new TrieNode();
                }
                node = node.branches[i];
                currentChar = (++index < stream.length) ? stream.charAt(index) : Trie.NULL_CHAR;
            }

            if (startIndex === index) {
                ++index;
                continue;
            }

            ++node.frequency;
            if (!node.isBanned && this.maxFrequencyNonBannedWord < node.frequency) {
                this.maxFrequencyNonBannedWord = node.frequency;
                this.nonBannedWordWithMaxFrequency = stream.substring(startIndex, index).toLowerCase();
            }
        }
    }

    /**
     * @param {string} character
     * @return {number}
     */
    codePoint(character) {
        return character.codePointAt(0);
    }

    /**
     * @param {string} character
     * @return {boolean}
     */
    isLetter(character) {
        return ('A' <= character && 'Z' >= character)
                || ('a' <= character && 'z' >= character);
    }
}
