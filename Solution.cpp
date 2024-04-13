
#include <array>
#include <cctype>
#include <string>
#include <vector>
#include <string_view>
using namespace std;

class Trie {

    struct TrieNode {

        static const int ALPHABET_SIZE = 26;
        array<shared_ptr<TrieNode>, ALPHABET_SIZE> branches{};
        bool isBanned{ false };
        int frequency{ 0 };
    };

    static const char NULL_CHAR = '\u0000';
    const shared_ptr<TrieNode> root{ make_shared<TrieNode>() };
    int maxFrequencyNonBannedWord = 0;


public:
    string nonBannedWordWithMaxFrequency;

    void addBannedWord(string_view word) const {
        shared_ptr<TrieNode> node = root;
        for (const auto& currentChar : word) {
            int index = tolower(currentChar) - 'a';
            if (node->branches[index] == nullptr) {
                node->branches[index] = make_shared<TrieNode>();
            }
            node = node->branches[index];
        }
        node->isBanned = true;
    }

    void extractAndAddAllWordsFromMixedStream(string_view stream) {
        int index = 0;

        while (index < stream.length()) {

            shared_ptr<TrieNode> node = root;
            int startIndex = index;
            char currentChar = stream[index];

            while (isalpha(currentChar)) {
                int i = tolower(currentChar) - 'a';
                if (node->branches[i] == nullptr) {
                    node->branches[i] = make_shared<TrieNode>();
                }
                node = node->branches[i];
                currentChar = (++index < stream.length()) ? stream[index] : NULL_CHAR;
            }

            if (startIndex == index) {
                ++index;
                continue;
            }

            ++node->frequency;
            if (!node->isBanned && maxFrequencyNonBannedWord < node->frequency) {
                maxFrequencyNonBannedWord = node->frequency;
                nonBannedWordWithMaxFrequency = stream.substr(startIndex, index - startIndex);
                for (auto& ch : nonBannedWordWithMaxFrequency) { ch = tolower(ch); }
            }
        }
    }
};


class Solution {
public:
    string mostCommonWord(const string& paragraph, const vector<string>& bannedWords) const {
        Trie trie;
        for (const auto& word : bannedWords) {
            trie.addBannedWord(word);
        }
        trie.extractAndAddAllWordsFromMixedStream(paragraph);
        return trie.nonBannedWordWithMaxFrequency;
    }
};
