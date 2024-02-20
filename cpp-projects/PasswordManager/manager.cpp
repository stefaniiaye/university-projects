#include <fstream>
#include <iostream>
#include <random>
#include <regex>
#include "manager.h"
#include <cctype>

auto encrypt(const std::string& str, const std::string& key) -> std::string {
    auto result = std::string();
    char a = 'A';
    for (int i = 0, j = 0; i < str.size(); i++, j++) {
        if (j >= key.size()) {
            result.push_back(str[i] + a);
            j = 0;
        }else result.push_back(str[i] + key[j]);
    }
    return result;
}
auto decrypt(const std::string& str, const std::string& key) -> std::string {
    auto result = std::string();
    char a = 'A';
    for (int i = 0, j = 0; i < str.size(); i++, j++) {
        if (j >= key.size()) {
            result.push_back(str[i] - a);
            j = 0;
        }else result.push_back(str[i] - key[j]);
    }
    return result;
}
auto isFileEmpty(const std::filesystem::path& filePath) -> bool {
    std::ifstream file(filePath.string());
    return file.peek() == std::ifstream::traits_type::eof();
}
auto addPassword(const std::filesystem::path& filePath, const std::string& key, std::vector<std::string>& entries,const std::string& timestamp) -> void {
    if(isFileEmpty("categories.txt")){
        std::cout << "You should create a category first\n";
        return;
    }else {
        std::string c = " ";
        std::string category;
        std::string name;
        std::string password;
        std::string login = " ";
        std::string website = " ";

        std::cout << "Chose one of the existing categories:\n";
        std::ifstream file("categories.txt");
        std::vector<std::string> categories;
        std::string line;

        while (std::getline(file, line)) {
            categories.push_back(line);
        }
        for (size_t i = 0; i < categories.size(); ++i) {
            std::cout << i + 1 << ". " << categories[i] << '\n';
        }
        std::cin >> c;
        for (auto &ch : c){
            if(!std::isdigit(ch)){
                try {
                    throw std::runtime_error("Invalid number, try again.\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                return;
            }
        }
        if(std::stoi(c) > categories.size() || std::stoi(c) <= 0){
            try {
                throw std::runtime_error("Invalid number, try again.\n");
            }
            catch(const std::exception& e){
                std::cerr << e.what() << std::endl;
            }
            return;
        }

        for (size_t i = 0; i < categories.size(); ++i){
            if(i+1 == std::stoi(c)){
                category = categories.at(i);
                break;
            }
        }
        std::cout << "Enter name of the password:\n";
        std::cin >> name;
        std::cout <<"Do you want to provide a login?\n 1 - yes\n 2 - no\n";
        std::cin >> c;
        while(c != "1" && c != "2"){
            try {
                throw std::runtime_error("Invalid number, try again.\n");
            }
            catch(const std::exception& e){
                std::cerr << e.what() << std::endl;
            }
            std::cin >> c;
        }
        if(c == "1"){
            std::cout <<"Enter login:\n";
            std::cin >> login;
        }
        std::cout <<"Do you want to provide a name of website or service?\n 1 - yes\n 2 - no\n";
        std::cin >> c;
        while(c != "1" && c != "2"){
            try {
                throw std::runtime_error("Invalid number, try again.\n");
            }
            catch(const std::exception& e){
                std::cerr << e.what() << std::endl;
            }
            std::cin >> c;
        }
        if(c == "1"){
            std::cout <<"Enter name of website:\n";
            std::cin >> website;
        }
        std::cout << "Do you want to generate a random password?\n 1 - yes\n 2 - no\n ";
        std::string choice;
        std::cin >> choice;
        if (choice == "1") {
            std::string length;
            bool includeUppercase = false, includeLowercase = false, includeSpecialChars = false;
            std::cout << "Enter the length of the password: ";
            std::cin >> length;
            for (auto &ch : length){
                if(!std::isdigit(ch)){
                    try {
                        throw std::runtime_error("Invalid number, try again.\n");
                    }
                    catch(const std::exception& e){
                        std::cerr << e.what() << std::endl;
                    }
                    return;
                }
            }
            std::cout << "Include lowercase letters? \n 1-yes\n 2-no\n ";
            std::cin >> choice;
            if(choice == "1") includeLowercase = true;
            else if(choice != "2"){
                try {
                    throw std::runtime_error("Invalid number, try again.\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                return;
            }
            std::cout << "Include uppercase letters? \n 1-yes\n 2-no\n ";
            std::cin >> choice;
            if(choice == "1") includeUppercase = true;
            else if(choice != "2"){
                try {
                    throw std::runtime_error("Invalid number, try again.\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                return;
            }
            std::cout << "Include special characters? \n 1-yes\n 2-no\n ";
            std::cin >> choice;
            if(choice == "1") includeSpecialChars = true;
            else if(choice != "2"){
                try {
                    throw std::runtime_error("Invalid number, try again.\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                return;
            }
            password = generatePassword(std::stoi(length), includeUppercase, includeLowercase, includeSpecialChars);
        } else if (choice == "2"){
            std::cout << "Enter the password: ";
            std::cin >> password;
        }
        else{
            try {
                throw std::runtime_error("Invalid number, try again.\n");
            }
            catch(const std::exception& e){
                std::cerr << e.what() << std::endl;
            }
            return;
        }
        int usages = passwordUsages(entries, password);
        if(usages > 0){
            std::cout << "The password has been used "<<usages<<" times already.\n";
        }else std::cout << "The password has never been used before.\n";
        double percent = securityCheck(password);
        std::cout << "You're password is "<<percent<<"% secure.\n";
        std::string newEntry = category + ";" + name + "," + login + "," + website + ";" + password;
        entries.push_back(newEntry);
        writeToFile(filePath,key,entries,timestamp);
        std::cout << "Password was added successfully.\n";
    }
}
auto generatePassword(int length, bool includeUppercase, bool includeLowercase, bool includeSpecialChars) -> std::string {
    std::string chars = "1234567890";
    if (includeUppercase)
        chars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    if (includeLowercase)
        chars += "abcdefghijklmnopqrstuvwxyz";
    if (includeSpecialChars)
        chars += "!@#$%^&*()";
    std::random_device rd;
    std::mt19937 gen(rd());
    std::shuffle(chars.begin(), chars.end(), gen);
    return chars.substr(0, length);
}
auto passwordUsages(const std::vector<std::string>& entries, const std::string& password) -> int {
    auto counter = std::ranges::count_if(entries, [&password](const std::string& entry) {
        auto parts = split(entry, ';');
        if (parts.size() >= 3) {
            return parts[2] == password;
        }
        return false;
    });
    return counter;
}
auto securityCheck(const std::string& password) -> double {
    std::regex uppercaseRegex("[A-Z]");
    std::regex lowercaseRegex("[a-z]");
    std::regex digitRegex("[0-9]");
    std::regex specialCharRegex("[^A-Za-z0-9]");

    bool hasUppercase = std::regex_search(password, uppercaseRegex);
    bool hasLowercase = std::regex_search(password, lowercaseRegex);
    bool hasDigit = std::regex_search(password, digitRegex);
    bool hasSpecialChar = std::regex_search(password, specialCharRegex);

    int numCharacterTypes = hasUppercase + hasLowercase + hasDigit + hasSpecialChar;

    double securityPercentage = (static_cast<double>(numCharacterTypes) / 4.0) * 100.0;

    return securityPercentage;
}
auto hideKey(const std::string& category, const std::string& key) -> std::string {
    std::string result = category;
    std::string resizedKey = key;
    while(resizedKey.length() > category.length()){
        for(int i = 0; i  < resizedKey.length()/2-1;i++){
            resizedKey[i] += resizedKey[resizedKey.length()-1-i];
            resizedKey.erase(resizedKey.length()-1-i);
        }
    }

    for(int i = 0,j = 0; i < category.length()-1; i++){
        if (j >= resizedKey.length()) {
            j = 0;
        }
        result[i] += resizedKey[j];
    }
    return result;
}
auto checkKey(const std::string& category, const std::string& key) -> std::string {
    std::string result = category;
    std::string resizedKey = key;
    while(resizedKey.length() > category.length()){
        for(int i = 0; i  < resizedKey.length()/2-1;i++){
            resizedKey[i] += resizedKey[resizedKey.length()-1-i];
            resizedKey.erase(resizedKey.length()-1-i);
        }
    }

    for(int i = 0,j = 0; i < category.length()-1; i++){
        if (j >= resizedKey.length()) {
            j = 0;
        }
        result[i] -= resizedKey[j];
    }
    return result;
}
auto split(const std::string& input, char delimiter) -> std::vector<std::string> {
    std::vector<std::string> minivec;
    std::stringstream ss(input);
    std::string minivecPart;
    while (std::getline(ss, minivecPart, delimiter)) {
        minivec.push_back(minivecPart);
    }
    return minivec;
}
auto writeToFile(const std::filesystem::path& filePath, const std::string& key,std::vector<std::string>& entries, const std::string& timestamp)  -> void {
    std::ofstream file(filePath,std::ios::out);
    auto splitedTime = split(timestamp, ' ');
    for(int i = 0; i < entries.size();i++){
        auto entryVec = split(entries.at(i),';');
        for(int j = 0; j < entryVec.size();j++ ){
            if(i == 0){
                if(j == 0)file << encrypt(hideKey(entryVec.at(j),key),key) << splitedTime[0] <<"\n";
                if(j == 1)file << splitedTime[1] << encrypt(entryVec.at(j),key) << "\n";
                if(j == 2)file << encrypt(entryVec.at(j),key) << splitedTime[2] << "\n";
            }else file << encrypt(entryVec.at(j),key) << "\n";
        }
    }
}
auto searchPasswords(const std::vector<std::string>& entries) -> std::vector<std::string> {
    std::cout << "By which parameter you would like to search for passwords:\n 1 - by category\n 2 - by name\n 3 - by login\n 4 - by website\n";
    std::string c = " ";
    std::cin >> c;
    while(c != "1" && c != "2" &&c != "3" && c != "4"){
        try {
            throw std::runtime_error("Invalid number, try again.\n");
        }
        catch(const std::exception& e){
            std::cerr << e.what() << std::endl;
        }
        std::cin >> c;
    }
    std::vector<std::string> resOfSearch;
    if(c == "1"){
        std::string a = " ";
        std::cout << "Chose one of the existing categories:\n";
        std::ifstream file("categories.txt");
        std::vector<std::string> categories;
        std::string line;

        while (std::getline(file, line)) {
            categories.push_back(line);
        }
        for (size_t i = 0; i < categories.size(); ++i) {
            std::cout << i + 1 << ". " << categories[i] << '\n';
        }
        std::cin >> a;
        for (auto &ch : a){
            if(!std::isdigit(ch)){
                return  std::vector<std::string>();
            }
        }
        if(std::stoi(a) > categories.size() || std::stoi(a) <= 0){
            return std::vector<std::string>();
        }
        std::string category;
        for (size_t i = 0; i < categories.size(); ++i){
            if(i+1 == std::stoi(a)){
                category = categories.at(i);
                break;
            }
        }
        for(int i = 0; i < entries.size();i++){
            auto parts = split(entries.at(i), ';');
            if(parts[0] == category){
                resOfSearch.push_back(entries.at(i));
            }
        }
    }else if(c == "2"){
        std::cout << "Enter a name of a password you would like to find:\n";
        std::string name;
        std::cin >> name;
        for(int i = 0; i < entries.size();i++){
            auto parts = split(entries.at(i), ';');
            auto parts1 = split(parts[1],',');
            if(parts1[0] == name) resOfSearch.push_back(entries.at(i));
        }
    }else if(c == "3"){
        std::cout << "Enter a login of a password you would like to find:\n";
        std::string login;
        std::cin >> login;
        for(int i = 0; i < entries.size();i++){
            auto parts = split(entries.at(i), ';');
            auto parts1 = split(parts[1],',');
            if(parts1[1] == login) resOfSearch.push_back(entries.at(i));
        }
    }else if(c == "4"){
        std::cout << "Enter a website or service of a password you would like to find:\n";
        std::string website;
        std::cin >> website;
        for(int i = 0; i < entries.size();i++){
            auto parts = split(entries.at(i), ';');
            auto parts1 = split(parts[1],',');
            if(parts1[2] == website) resOfSearch.push_back(entries.at(i));
        }
    }
    return resOfSearch;
}
auto sortPasswords(const std::vector<std::string>& entries) -> std::vector<std::string> {
    std::cout << "By which parameter you would like to sort passwords:\n 1 - by category\n 2 - by name\n 3 - by login"
                 "\n 4 - by website\n 5 - by name and category\n 6 - by login and category\n 7 - by website and category\n";
    std::string c = " ";
    std::cin >> c;
    while(c != "1" && c != "2" && c!= "3" && c != "4" && c != "5"  && c != "6" && c != "7"){
        try {
            throw std::runtime_error("Invalid number, try again.\n");
        }
        catch(const std::exception& e){
            std::cerr << e.what() << std::endl;
        }
        std::cin >> c;
    }
    std::vector<std::string> resOfSort = entries;
    auto comparator = [&](const std::string& entry1, const std::string& entry2) {
        std::vector<std::string> parts1 = split(entry1, ';');
        std::vector<std::string> parts2 = split(entry2, ';');
        std::vector<std::string> parts11 = split(parts1[1],',');
        std::vector<std::string> parts22 = split(parts2[1],',');
    if(c == "1"){
        return parts1[0] < parts2[0];
    }else if(c == "2") {
        return parts11[0] < parts22[0];
    }else if (c == "3"){
        return parts11[1] < parts22[1];
    }else if (c == "4"){
        return parts11[2] < parts22[2];
    }else if (c == "5") {
        if (parts11[0] != parts22[0]) {
            return parts11[0] < parts22[0];
        } else {
            return parts1[0] < parts2[0];
        }
    }else if (c == "6") {
        if (parts11[1] != parts22[1]) {
            return parts11[1] < parts22[1];
        } else {
            return parts1[0] < parts2[0];
        }
    }else if (c == "7") {
        if (parts11[2] != parts22[2]) {
            return parts11[2] < parts22[2];
        } else {
            return parts1[0] < parts2[0];
        }
    } else {
        return false;
    }};
    std::ranges::sort(resOfSort,comparator);
    return resOfSort;
}
auto editPassword(const std::filesystem::path& filePath, const std::string& key, std::vector<std::string>& entries,const std::string& timestamp) -> void {
    std::cout << "Firstly, you need to chose record that you would like to edit.\n";
    std::vector<std::string> possiblePass = searchPasswords(entries);
    if(possiblePass.size() > 0){
        std::string c = " ";
        std::cout << "Chose one of them:\n";
        std::string *recToChange;
        std::string recStr;
        printVec(possiblePass);
        std::cin >> c;
        for (auto &ch : c){
            if(!std::isdigit(ch)){
                try {
                    throw std::runtime_error("Invalid number, try again.\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                return;
            }
        }
        if(std::stoi(c) > possiblePass.size() || std::stoi(c) <= 0){
            try {
                throw std::runtime_error("Invalid number, try again.\n");
            }
            catch(const std::exception& e){
                std::cerr << e.what() << std::endl;
            }
            return;
        }

        for (size_t i = 0; i < possiblePass.size(); i++){
            if(i+1 == std::stoi(c)){
                recStr = possiblePass.at(i);
                break;
            }
        }
        for(size_t i = 0; i < entries.size(); i++){
            if(entries.at(i) == recStr){
                recToChange = &entries.at(i);
                break;
            }
        }
        std::cout << "What would you like to change?\n 1 - category\n 2 - name\n 3 - password\n 4 - login\n 5 - website\n";
        std::cin >> c;
        while(c != "1" && c != "2" && c!= "3" && c != "4" && c != "5"){
            try {
                throw std::runtime_error("Invalid number, try again.\n");
            }
            catch(const std::exception& e){
                std::cerr << e.what() << std::endl;
            }
            std::cin >> c;
        }
        auto parts = split(*recToChange,';');
        auto parts1 = split(parts[1], ',');
        std::string res;
        if(c == "1"){
            std::string category;
            std::cout << "Chose one of the existing categories to which you would like to change:\n";
            std::ifstream file("categories.txt");
            std::vector<std::string> categories;
            std::string line;

            while (std::getline(file, line)) {
                categories.push_back(line);
            }
            for (size_t i = 0; i < categories.size(); ++i) {
                std::cout << i + 1 << ". " << categories[i] << '\n';
            }
            std::cin >> c;
            for (auto &ch : c){
                if(!std::isdigit(ch)){
                    try {
                        throw std::runtime_error("Invalid number, try again.\n");
                    }
                    catch(const std::exception& e){
                        std::cerr << e.what() << std::endl;
                    }
                    return;
                }
            }
            if(std::stoi(c) > categories.size() || std::stoi(c) <= 0){
                try {
                    throw std::runtime_error("Invalid number, try again.\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                return;
            }

            for (size_t i = 0; i < categories.size(); ++i){
                if(i+1 == std::stoi(c)){
                    category = categories.at(i);
                    break;
                }
            }
            res = category+";"+parts[1]+";"+parts[2];
        }else if (c == "2"){
            std::cout << "Enter a new name of a password:\n";
            std::string name;
            std::cin >> name;
            parts1.at(0) = name;
            res = parts[0]+";"+parts1[0] + "," + parts1[1] + "," + parts1[2] +";"+parts[2];
        }else if(c == "3") {
            std::string password;
            std::cout << "Do you want to generate a random password?\n 1-yes\n 2-no\n ";
            std::string choice;
            std::cin >> choice;
            if (choice == "1") {
                std::string length;
                bool includeUppercase = false, includeLowercase = false, includeSpecialChars = false;
                std::cout << "Enter the length of the password: ";
                std::cin >> length;
                for (auto &ch : length){
                    if(!std::isdigit(ch)){
                        try {
                            throw std::runtime_error("Invalid number, try again.\n");
                        }
                        catch(const std::exception& e){
                            std::cerr << e.what() << std::endl;
                        }
                        return;
                    }
                }
                std::cout << "Include lowercase letters? \n 1-yes\n 2-no\n ";
                std::cin >> choice;
                if(choice == "1") includeLowercase = true;
                else if(choice != "2"){
                    try {
                        throw std::runtime_error("Invalid number, try again.\n");
                    }
                    catch(const std::exception& e){
                        std::cerr << e.what() << std::endl;
                    }
                    return;
                }
                std::cout << "Include uppercase letters? \n 1-yes\n 2-no\n ";
                std::cin >> choice;
                if(choice == "1") includeUppercase = true;
                else if(choice != "2"){
                    try {
                        throw std::runtime_error("Invalid number, try again.\n");
                    }
                    catch(const std::exception& e){
                        std::cerr << e.what() << std::endl;
                    }
                    return;
                }
                std::cout << "Include special characters? \n 1-yes\n 2-no\n ";
                std::cin >> choice;
                if(choice == "1") includeSpecialChars = true;
                else if(choice != "2"){
                    try {
                        throw std::runtime_error("Invalid number, try again.\n");
                    }
                    catch(const std::exception& e){
                        std::cerr << e.what() << std::endl;
                    }
                    return;
                }
                password = generatePassword(std::stoi(length), includeUppercase, includeLowercase, includeSpecialChars);
            } else if (choice == "2"){
                std::cout << "Enter the password: ";
                std::cin >> password;
            }
            else{
                try {
                    throw std::runtime_error("Invalid number, try again.\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                return;
            }
            int usages = passwordUsages(entries, password);
            if(usages > 0){
                std::cout << "The password has been used "<<usages<<" times already.\n";
            }else std::cout << "The password has never been used before.\n";
            double percent = securityCheck(password);
            std::cout << "You're password is "<<percent<<"% secure.\n";

            res = parts[0]+";"+parts[1]+";"+password;
        }
        else if (c == "4"){
            std::cout << "Enter a new login to a password:\n";
            std::string login;
            std::cin >> login;
            parts1.at(1) = login;
            res = parts[0]+";"+parts1[0] + "," + parts1[1] + "," + parts1[2] +";"+parts[2];
        }
        else if (c == "5"){
            std::cout << "Enter a new name of website or service to a password:\n";
            std::string website;
            std::cin >> website;
            parts1.at(0) = website;
            res = parts[0]+";"+parts1[0] + "," + parts1[1] + "," + parts1[2] +";"+parts[2];
        }
        *recToChange = res;
        writeToFile(filePath,key,entries,timestamp);
        std::cout << "Data was edited successfully.\n";
    }else{
        std::cout << "No results.\n";
        return;
    }
}
auto printVec(const std::vector<std::string>& vec) -> void {
    int counter = 1;
    for(std::string v:vec){
        auto parts = split(v,';');
        auto parts1 = split(parts[1],',');
        if(parts1[1] == " " && parts1[2] == " "){
            std::cout <<counter<<"." << " Category: "<<parts[0]<<", name: "<<parts1[0]<<", password: "<<parts[2]<<std::endl;
        }else if (parts1[1] != " " && parts1[2] == " "){
            std::cout <<counter<<"."<< " Category: "<<parts[0]<<", name: "<<parts1[0]<<", login: "<<parts1[1] <<", password: "<<parts[2]<<std::endl;
        }else if (parts1[1] == " " && parts1[2] != " "){
            std::cout <<counter<<"."<< " Category: "<<parts[0]<<", name: "<<parts1[0]<<", website: "<<parts1[2] <<", password: "<<parts[2]<<std::endl;
        }else std::cout <<counter<<"."<< " Category: "<<parts[0]<<", name: "<<parts1[0]<< ", login: "<<parts1[1]
        <<", website: "<<parts1[2]<<", password: "<<parts[2]<<std::endl;
        counter++;
    }
}
auto deleteRecord(const std::filesystem::path& filePath, const std::string& key, std::vector<std::string>& entries,const std::string& timestamp) -> void {
    std::vector<std::string> recordsToDel;
    for(bool isRunning = true; isRunning;) {
        std::cout << "You need to chose record that you would like to delete.\n";
        std::vector<std::string> possiblePass = searchPasswords(entries);
        if (possiblePass.size() > 0) {
            std::string c = " ";
            std::cout << "Chose the password you like to delete:\n";
            printVec(possiblePass);
            std::cin >> c;
            for (auto &ch : c){
                if(!std::isdigit(ch)){
                    try {
                        throw std::runtime_error("Invalid number, try again.\n");
                    }
                    catch(const std::exception& e){
                        std::cerr << e.what() << std::endl;
                    }
                    return;
                }
            }
            if(std::stoi(c) > possiblePass.size() || std::stoi(c) <= 0){
                try {
                    throw std::runtime_error("Invalid number, try again.\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                return;
            }

            for (size_t i = 0; i < possiblePass.size(); i++) {
                if (i + 1 == std::stoi(c)) {
                    recordsToDel.push_back(possiblePass.at(i));
                    break;
                }
            }
            std::cout << "Do you want to chose more passwords to delete?\n 1 - yes\n 2 - no\n";
            std::string b = " ";
            std::cin >> b;
            while(b != "1" && b != "2"){
                try {
                    throw std::runtime_error("Invalid number, try again.\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                std::cin >> b;
            }
            if(b == "2"){
                isRunning = false;
            }
        }
        else{
            std::cout << "No results.\n";
            return;
        }
    }
    //mistake3
    for (size_t i = 0; i < recordsToDel.size();i++) {
        for (size_t j = 0; j < entries.size(); j++) {
            if (entries.at(j) == recordsToDel.at(i)) {
                entries.erase(entries.begin() + j);
                break;
            }
        }
    }
    writeToFile(filePath,key,entries,timestamp);
    std::cout << recordsToDel.size() << " password(s) were successfully deleted.\n";
}
auto addCategory() -> void {
    std::ifstream file("categories.txt");
    std::vector<std::string> categories;
    std::string line;
    while (std::getline(file, line)) {
        categories.push_back(line);
    }
    std::string newCat;
    std::cout << "Enter a name for new category:\n";
    std::cin >> newCat;
    for (auto &cat : categories){
        if(cat == newCat){
            std::cout << "Category "<<newCat<<" already exists.\n";
            return;
        }
    }
    std::ofstream fileC("categories.txt",std::ios::app);
    fileC << "\n"<< newCat;
    std::cout << "Category successfully added to list.\n";

}
auto deleteCategory(const std::filesystem::path& filePath, const std::string& key,std::vector<std::string>& entries,const std::string& timestamp) -> void {
    std::cout << "Chose category you would like to delete:\n";
    std::ifstream file("categories.txt");
    std::vector<std::string> categories;
    std::string line;
    std::string c = " ";
    std::string category;
    std::vector<std::string> entriesToDelete;

    while (std::getline(file, line)) {
        categories.push_back(line);
    }
    for (size_t i = 0; i < categories.size(); ++i) {
        std::cout << i + 1 << ". " << categories[i] << '\n';
    }
    std::cin >> c;
    for (auto &ch : c){
        if(!std::isdigit(ch)){
            try {
                throw std::runtime_error("Invalid number, try again.\n");
            }
            catch(const std::exception& e){
                std::cerr << e.what() << std::endl;
            }
            return;
        }
    }
    if(std::stoi(c) > categories.size() || std::stoi(c) <= 0){
        try {
            throw std::runtime_error("Invalid number, try again.\n");
        }
        catch(const std::exception& e){
            std::cerr << e.what() << std::endl;
        }
        return;
    }
    for (size_t i = 0; i < categories.size(); ++i){
        if(i+1 == std::stoi(c)){
            category = categories.at(i);
            categories.erase(categories.begin() + i);
            break;
        }
    }
    for(int i = 0; i < entries.size();i++){
        auto entryVec = split(entries.at(i),';');
        if(entryVec.at(0) == category)
        entriesToDelete.push_back(entries.at(i));
    }
    //mistake4
    for (size_t i = 0; i < entriesToDelete.size();i++) {
        for (size_t j = 0; j < entries.size(); j++) {
            if (entries.at(j) == entriesToDelete.at(i)) {
                entries.erase(entries.begin() + j);
                break;
            }
        }
    }
    writeToFile(filePath,key,entries,timestamp);
    std::ofstream fileC("categories.txt", std::ios::out);
    for(auto &cat : categories){
        fileC << cat << "\n";
    }
    std::cout <<"Category " << category<<" was successfully deleted with all("<< entriesToDelete.size() << ") password(s) assigned to it.\n";
}