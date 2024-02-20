#pragma once
#include <string>
#include <filesystem>
#include <vector>

/**
 * @brief Encrypts a string using a specified key.
 *
 * @param str The string to be encrypted.
 * @param key The encryption key.
 * @return The encrypted string.
 */
auto encrypt(const std::string& str, const std::string& key) -> std::string;

/**
 * @brief Decrypts an encrypted string using a specified key.
 *
 * @param str The encrypted string to be decrypted.
 * @param key The encryption key.
 * @return The decrypted string.
 */
auto decrypt(const std::string& str, const std::string& key) -> std::string;

/**
 * @brief Checks if a file is empty.
 *
 * @param filePath The path to the file.
 * @return `true` if the file is empty, `false` otherwise.
 */
auto isFileEmpty(const std::filesystem::path& filePath) -> bool;

/**
 * @brief Adds a new entry to a file.
 *
 * @param filePath The path to the file.
 * @param key The encryption key.
 * @param entries A vector of existing password entries.
 * @param timestamp The timestamp of the last encryption attempt.
 */
auto addPassword(const std::filesystem::path& filePath, const std::string& key, std::vector<std::string>& entries,const std::string& timestamp) -> void;

/**
 * @brief Hides a key within a category.
 *
 * @param category The category to hide the key in.
 * @param key The key to be hidden.
 * @return The hidden key.
 */
auto hideKey(const std::string& category, const std::string& key) -> std::string;

/**
 * @brief Checks a key within a category.
 *
 * @param category The category to check the key in.
 * @param key The key to be checked.
 * @return The checked key.
 */
auto checkKey(const std::string& category, const std::string& key) -> std::string;

/**
 * @brief Splits a string into a vector of substrings based on a delimiter.
 *
 * @param input The input string.
 * @param delimiter The delimiter character.
 * @return A vector of substrings.
 */
auto split(const std::string& input, char delimiter) -> std::vector<std::string>;

/**
 * @brief Writes password entries to a file.
 *
 * @param filePath The path to the file.
 * @param key The encryption key.
 * @param entries A vector of password entries.
 * @param timestamp The timestamp of the password entries.
 */
auto writeToFile(const std::filesystem::path& filePath, const std::string& key,std::vector<std::string>& entries, const std::string& timestamp)  -> void;

/**
 * @brief Generates a password with specified properties.
 *
 * @param length The length of the password.
 * @param includeUppercase Whether to include uppercase letters.
 * @param includeLowercase Whether to include lowercase letters.
 * @param includeSpecialChars Whether to include special characters.
 * @return The generated password.
 */
auto generatePassword(int length, bool includeUppercase, bool includeLowercase, bool includeSpecialChars) -> std::string;

/**
 * @brief Counts the number of times a password is used in the entries.
 *
 * @param entries A vector of password entries.
 * @param password The password to search for.
 * @return The number of times the password is used.
 */
auto passwordUsages(const std::vector<std::string>& entries, const std::string& password) -> int;

/**
 * @brief Performs a security check on a password based on some parameters and returns a security percentage.
 *
 * @param password The password to perform the security check on.
 * @return The security percentage of the password.
 */
auto securityCheck(const std::string& password) -> double;

/**
 * @brief Searches for passwords based on user choice by which parameter to search.
 *
 * @param entries A vector of password entries.
 * @return A vector of found password entries.
 */
auto searchPasswords(const std::vector<std::string>& entries) -> std::vector<std::string>;

/**
 * @brief Sorts passwords based on user choice by which parameter to sort.
 *
 * @param entries A vector of password entries.
 * @return A vector of sorted password entries.
 */
auto sortPasswords(const std::vector<std::string>& entries) -> std::vector<std::string>;

/**
 * @brief Allows the user to edit a specific password record based on his choice.
 *
 * @param filePath The path to the file where the passwords are stored.
 * @param key The encryption key used to access the passwords.
 * @param entries The vector of password entries.
 * @param timestamp The timestamp of the last modification to the password file.
 */
auto editPassword(const std::filesystem::path& filePath, const std::string& key, std::vector<std::string>& entries,const std::string& timestamp) -> void;

/**
 * @brief Prints the contents of a vector of strings.
 *
 * @param vec The vector of strings to be printed.
 * @return void
*/
auto printVec(const std::vector<std::string>& vec) -> void;

/**
 * Deletes a record from a vector of entries and writes the updated entries to a file.
 *
 * @param filePath The path to the file where the entries are stored.
 * @param key The encryption key used to encrypt the entries.
 * @param entries The vector of strings containing the entries.
 * @param timestamp The timestamp indicating the last modification time of the file.
 * @return void
 */
auto deleteRecord(const std::filesystem::path& filePath, const std::string& key, std::vector<std::string>& entries,const std::string& timestamp) -> void;

/**
 * Adds a new category to the list of categories stored in the "categories.txt" file.
 *
 * @return void
 */
auto addCategory() -> void;

/**
 * Deletes a category and all passwords assigned to it from the list of categories and entries.
 * Updates the "categories.txt" file and writes the updated entries to a file.
 *
 * @param filePath The path to the file where the entries are stored.
 * @param key The encryption key used to encrypt the entries.
 * @param entries The vector of strings containing the entries.
 * @param timestamp The timestamp indicating the last modification time of the file.
 * @return void
 */
auto deleteCategory(const std::filesystem::path& filePath, const std::string& key,std::vector<std::string>& entries,const std::string& timestamp) -> void;