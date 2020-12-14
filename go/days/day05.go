package days

import (
	"io/ioutil"
	"math"
	"sort"
	. "strings"
)

func findSeatPart(str string, min int, max int) int {
	length := len(str)
	options := int(math.Pow(2, float64(length-1)))

	if HasPrefix(str, "F") || HasPrefix(str, "L") {
		if length == 1 {
			return min
		}
		return findSeatPart(str[1:], min, max-options)
	} else if HasPrefix(str, "B") || HasPrefix(str, "R") {
		if length == 1 {
			return max
		}
		return findSeatPart(str[1:], min+options, max)
	}

	return 0
}

func parseBoardingPasses(str string) []int {
	passes := Split(str, "\n")
	rowParts := 7
	columnParts := 3
	var seats []int

	for _, pass := range passes {
		if len(pass) > 0 {
			row := findSeatPart(pass[:rowParts], 0, int(math.Pow(2, float64(rowParts)))-1)
			column := findSeatPart(pass[len(pass)-columnParts:], 0, int(math.Pow(2, float64(columnParts)))-1)
			seatId := row*8 + column
			seats = append(seats, seatId)
			println(row, ",", column)
		}
	}
	sort.Ints(seats)
	return seats
}

func findMySeatId(seats []int) int {
	for i, seat := range seats {
		if seats[i+1] == seat+2 {
			return seat + 1
		}
	}
	return 0
}

func Day05() {
	dat, err := ioutil.ReadFile("./days/inputs/05.txt")
	check(err)
	seats := parseBoardingPasses(string(dat))
	println("Last seat: ", seats[len(seats)-1])
	println("My seat:", findMySeatId(seats))
}
