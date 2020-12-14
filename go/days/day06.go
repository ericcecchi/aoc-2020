package days

import (
	"io/ioutil"
	"strings"
)

func getAnyAnswers(group string) *set {
	answers := NewSet()
	for _, char := range group {
		if string(char) == "\n" {
			continue
		}
		answers.Add(string(char))
	}
	return answers
}

func getAllAnswers(group string) *set {
	answers := make(map[string]int)
	allAnswers := NewSet()
	people := strings.Split(group, "\n")
	for _, person := range people {
		for _, char := range person {
			_, ok := answers[string(char)]
			if !ok {
				answers[string(char)] = 1
			} else {
				answers[string(char)] += 1
			}
		}
	}

	for answer, count := range answers {
		if count == len(people) {
			allAnswers.Add(answer)
		}
	}
	return allAnswers
}

func parseGroups(str string) []string {
	return strings.Split(str, "\n\n")
}

func getTotalAnyAnswers(data string) int {
	groups := parseGroups(data)
	var total int
	for _, group := range groups {
		answers := getAnyAnswers(strings.Trim(group, "\n"))
		total += len(answers.m)
	}
	return total
}

func getTotalAllAnswers(data string) int {
	groups := parseGroups(data)
	var total int
	for _, group := range groups {
		answers := getAllAnswers(strings.Trim(group, "\n"))
		total += len(answers.m)
	}
	return total
}

func Day06() {
	data, err := ioutil.ReadFile("./days/inputs/06.txt")
	check(err)
	println("Total any answers:", getTotalAnyAnswers(string(data)))
	println("Total all answers:", getTotalAllAnswers(string(data)))
}
