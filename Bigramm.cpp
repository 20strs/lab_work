#include <iostream>
#include <fstream>
#include <map>
#include <vector>
#include <set>

using namespace std;

set<string> set_bigrams(const string &s) {
    set<string> out;
    if (s.length() == 1) out.insert(s);
    for (size_t i = 0; i + 1 < s.length(); i++) {
        out.insert(s.substr(i, 2));
    }
    return out;
}

void reading_file(map<string, pair<set<string>, int>> &dict) {
    int popularity=0;
    //ifstream file("/home/roman/ClionProjects/count_big.txt");
    ifstream file("count_big.txt");
    string word;
    while (file) {
        file >> word >> popularity;
        dict[word].second = popularity;
        dict[word].first = set_bigrams(word);
    }
    file.close();
}

void read_in(vector<string> &in) {
    string line;
    while (getline(cin, line)) {
        if (line == "") break;
        in.push_back(line);
    }
}

double similarity(const set<string> &a,const set<string> &b) {
    int intersection=0;
    for (string s:b)
        if (a.count(s)) intersection++;
    return (double )intersection / (double )(a.size() + b.size() - intersection);
}

string find_result(const map<string, pair<set<string>, int>> &dict, string word) {
    set<string> bigrams = set_bigrams(word);
    string ans="";
    double cur_similarity = 0;
    int pop = 0;

    for (auto pair: dict) {
        double buf = similarity(bigrams, pair.second.first);

        if ((cur_similarity < buf) ||
            (cur_similarity <= buf && pop < pair.second.second) ||
            (cur_similarity <= buf && pop <= pair.second.second && pair.first.length() < ans.length())) {
            pop = pair.second.second;
            cur_similarity = buf;
            ans = pair.first;
        }
    }
    return ans;
}

void error_correct(const vector<string> &in, vector<string> &out,const map<string, pair<set<string>, int>> &dict) {
    for (string s: in) {
        out.push_back(find_result(dict, s));
    }
}

int main() {
    vector<string> in, out;

    map<string, pair<set<string>, int>> global_dict;
    reading_file(global_dict);

    read_in(in);

    error_correct(in, out, global_dict);

    for (string s:out) cout << s << endl;

    return 0;
}
