#include <iostream>
#include <string>
#include <filesystem>
#include <vector>
#include <fstream>
#include "manager.h"

/**
 * @brief Assigns a source file to be used for the program's operations.
 * Provides options to select an existing file, use a default file, or enter an absolute path to the file.
 *
 * @return void
 */
auto assignSourceFile() -> void;
/**
 * @brief Takes a selected file path and prompts the user to choose various commands related to password management.
 * Depending on the selected command, it performs operations such as searching passwords, sorting passwords,
 * adding passwords, editing passwords, deleting passwords, adding categories, and deleting categories.
 *
 * @param filePath The path of the selected file to perform operations on.
 * @return void
 */
auto choseCommand(const std::filesystem::path& filePath) -> void;
/**
 * @brief Reads the entries from the specified file path and decrypts the name and password fields using the provided key.
 * Constructs a vector of strings representing the decrypted entries.
 *
 * @param filePath   The path of the file to read entries from.
 * @param firstLine  The first line of the file.
 * @param key        The decryption key.
 * @return           A vector of decrypted entries in the format "category;name;password".
 */
auto writeEntries(const std::filesystem::path& filePath, const std::string& firstLine,const std::string& key ) -> std::vector<std::string>;
/**
 * @brief Adds a timestamp to the specified file by modifying the first line, second line, and third line of the file
 * The modified lines are then written back to the file.
 *
 * @param filePath   The path of the file to add the timestamp to.
 * @param timestamp  The timestamp string to add.
 * @return void
 */
auto addTimestamp(const std::filesystem::path& filePath, const std::string& timestamp) -> void;

int main() {
    assignSourceFile();
    return 0;
}

auto assignSourceFile() -> void {
    auto sourceFile = std::filesystem::path();
    for(bool isRunning = true; isRunning;) {
        std::string choice = " ";
        std::cout << "(1) Select an existing file as a source file\n";
        std::cout << "(2) Use default source file\n";
        std::cout << "(3) Enter absolute path to the file\n";
        std::cout << "(4) Safe exit\n";
        std::cin >> choice;

        if (choice == "1") {
            auto path = std::filesystem::path("C:\\C++\\PJC_PRO\\cmake-build-debug\\files");
            auto files = std::vector<std::filesystem::directory_entry>();
            auto iter = std::filesystem::recursive_directory_iterator(path);
            for (auto const &entry: iter) {
                if (entry.is_regular_file() && entry.path().extension() == ".txt") files.push_back(entry);
            }
            if (files.empty()) {
                std::cout << "There are no txt files in " << path << ", try again.\n\n";
                continue;
            }
            std::cout << path << ":\n";
            for (int i = 0; i < files.size(); i++) {
                std::cout << i + 1 << ". " << files[i].path().string().erase(0, path.string().size() + 1) << "\n";
            }

            int fileInd;
            std::cout << "Enter the number of the file you want to choose as source file:\n ";
            std::cin >> fileInd;

            if (fileInd < 1 || fileInd > files.size()) {
                std::cout << "Invalid selection, please try again.\n\n";
                continue;
            }
            sourceFile = files[fileInd - 1].path();
        } else if (choice == "2") {
            sourceFile = std::filesystem::path("C:\\C++\\PJC_PRO\\cmake-build-debug\\files\\default.txt");
        } else if (choice == "3") {
            std::string absolutePath;
            std::cout << "Enter the absolute path to the file: \n";
            std::cin >> absolutePath;
            sourceFile = std::filesystem::path(absolutePath);
        }
        else if(choice == "4") isRunning = false;
        else {
            try {
                throw std::runtime_error("Invalid number, try again.\n");
            }
            catch(const std::exception& e){
                std::cerr << e.what() << std::endl;
            }
            continue;
        }
        if(choice != "4") choseCommand(sourceFile);
    }
}
auto choseCommand(const std::filesystem::path& filePath) -> void{
    std::string c = " ";
    auto entries = std::vector<std::string>();
    for ( bool isRunning = true; isRunning;){

        auto currentTime = std::chrono::system_clock::now();
        std::time_t timestamp = std::chrono::system_clock::to_time_t(currentTime);
        std::stringstream ss;
        ss << std::put_time(std::localtime(&timestamp), "%Y#$%H %m#$%M %d#$%S");
        std::string timestampString = ss.str();

        std::string masterPassword;
        if(isFileEmpty(filePath)){
            std::cout << "File is empty, create a password for it:\n";
            std::cin >> masterPassword;

            if(isFileEmpty("categories.txt")){
                std::cout << "Chose one of the possible commands:\n";
                std::cout << "(3) Add password\n";
                std::cout << "(6) Add category\n";
                std::cout << "(8) Safe exit\n";
                std::cin >> c;
                if(c != "3" && c != "6" && c != "8"){
                    try {
                        throw std::runtime_error("Invalid number, try again.\n");
                    }
                    catch(const std::exception& e){
                        std::cerr << e.what() << std::endl;
                    }
                    continue;
                }
            }else{
                std::cout << "Chose one of the possible commands:\n";
                std::cout << "(3) Add password\n";
                std::cout << "(6) Add category\n";
                std::cout << "(7) Delete category\n";
                std::cout << "(8) Safe exit\n";
                std::cin >> c;
                if(c != "3" && c != "6" && c != "8" && c != "7"){
                    try {
                        throw std::runtime_error("Invalid number, try again.\n");
                    }
                    catch(const std::exception& e){
                        std::cerr << e.what() << std::endl;
                    }
                    continue;
                }
            }
        }
        else{

            std::cout << "Enter a password for chosen file:\n";
            std::cin >> masterPassword;
            addTimestamp(filePath,timestampString);
            std::string firstCat;
            std::fstream file(filePath);
            std::getline(file, firstCat);
            firstCat = firstCat.substr(0,firstCat.length()-8);
            auto dectFirstCat = decrypt(firstCat,masterPassword);
            auto possibleCat = checkKey(dectFirstCat,masterPassword);

            std::ifstream fileC("categories.txt");
            std::vector<std::string> categories;
            std::string line;
            while (std::getline(fileC, line)) {
                categories.push_back(line);
            }
            auto isPassCorrect = false;
            for(int i = 0;i < categories.size();i++){
                if(categories.at(i)==possibleCat){
                    isPassCorrect = true;
                    break;
                }
            }
            if(!isPassCorrect) {
                try {
                    throw std::runtime_error("Wrong password, to try again press 1, or press 2 to exit:\n");
                }
                catch(const std::exception& e){
                    std::cerr << e.what() << std::endl;
                }
                std::cin >> c;
                if(c == "2"){
                    isRunning = false;
                }
                else if(c != "1"){
                    try {
                        throw std::runtime_error("Invalid number, try again.\n");
                    }
                    catch(const std::exception& e){
                        std::cerr << e.what() << std::endl;
                    }
                }
                continue;
            }
            entries = writeEntries(filePath,possibleCat,masterPassword);
            std::cout << "Chose one of the possible commands:\n";
            std::cout << "(1) Search passwords\n";
            std::cout << "(2) Sort passwords\n";
            std::cout << "(3) Add password\n";
            std::cout << "(4) Edit password\n";
            std::cout << "(5) Delete password(s)\n";
            std::cout << "(6) Add category\n";
            std::cout << "(7) Delete category\n";
            std::cout << "(8) Safe exit\n";
            std::cin >> c;
        }
        if(c == "1"){
            auto resultOfSearch = searchPasswords(entries);
            if (resultOfSearch.size() > 0) {
                std::cout << "Here is result of you're search:\n";
                printVec(resultOfSearch);
            } else {
                std::cout << "No passwords found.\n";
            }
        }
        else if(c == "2"){
            auto resultOfSort = sortPasswords(entries);
            std::cout << "Here is result of sorting:\n";
            printVec(resultOfSort);
        }
        else if(c == "3"){
            addPassword(filePath,masterPassword,entries,timestampString);
        }
        else if(c == "4"){
            editPassword(filePath,masterPassword,entries,timestampString);
        }
        else if(c == "5"){
            deleteRecord(filePath,masterPassword,entries,timestampString);
        }
        else if(c == "6"){
            addCategory();
        }
        else if(c == "7"){
            deleteCategory(filePath,masterPassword,entries,timestampString);
        }
        else if(c == "8"){
            isRunning = false;
        }
        else {
            try {
                throw std::runtime_error("Invalid number, try again.\n");
            }
            catch(const std::exception& e){
                std::cerr << e.what() << std::endl;
            }
        }
    }
}
auto writeEntries(const std::filesystem::path& filePath, const std::string& firstLine,const std::string& key) -> std::vector<std::string> {
    std::ifstream file(filePath);
    std::vector<std::string> entries;
    std::string category, name, password;
    int entryCounter = 0;
    while (std::getline(file, category) && std::getline(file, name) && std::getline(file, password)) {
        entryCounter++;
        if(entryCounter == 1){
            //mistake1
            name = name.substr(6,name.length());
            password = password.substr(0,password.length()-6);
            std::string entry = firstLine + ";" + decrypt(name,key) + ";" + decrypt(password,key);
            entries.push_back(entry);
        }else {
            std::string entry = decrypt(category,key) + ";" + decrypt(name,key) + ";" + decrypt(password,key);
            entries.push_back(entry);
        }
    }
    return entries;
}
auto addTimestamp(const std::filesystem::path& filePath, const std::string& timestamp) -> void{
    std::ifstream file(filePath);
    std::vector<std::string> entries;
    std::string firstLine, secLine, thirdLine;
    int entryCounter = 1;
    auto splitedTime = split(timestamp,' ');
    while (std::getline(file, firstLine) && std::getline(file, secLine) && std::getline(file, thirdLine)) {
        if(entryCounter == 1){
            //mistake2
            firstLine = firstLine.substr(0,firstLine.length()-8);
            secLine = secLine.substr(6,secLine.length());
            thirdLine = thirdLine.substr(0,thirdLine.length()-6);
            entries.push_back(firstLine + splitedTime[0]);
            entries.push_back(splitedTime[1] + secLine);
            entries.push_back(thirdLine + splitedTime[2]);
            entryCounter++;
        }else {
            entries.push_back(firstLine);
            entries.push_back(secLine);
            entries.push_back(thirdLine);
        }
    }
    std::ofstream fileF(filePath,std::ios::out);
    for(int i = 0; i < entries.size();i++){
        fileF << entries.at(i) <<"\n";
    }
}