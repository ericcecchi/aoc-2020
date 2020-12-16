package days

import (
	"fmt"
	"github.com/yourbasic/graph"
	"regexp"
	"strconv"
	"strings"
)

type BagContents struct {
	Count int    `json:"count"`
	Type  string `json:"type"`
}

type Bag struct {
	Type     string        `json:"type"`
	Contents []BagContents `json:"contents"`
}

type Bags []Bag

func (b Bags) FindIndex(bagType string) int {
	for i, bag := range b {
		if bag.Type == bagType {
			return i
		}
	}

	return -1
}

func parseBags(str string) Bags {
	re := regexp.MustCompile(` ?([0-9*]) ([a-z ]*?) bags?[,.]`)
	bags := strings.Split(str, "\n")
	var parsedBags Bags
	for _, bag := range bags {
		bagAndContents := strings.Split(bag, " bags contain ")
		innerBags := re.FindAllStringSubmatch(bagAndContents[1], -1)
		var parsedInnerBags []BagContents
		for _, innerBag := range innerBags {
			count, err := strconv.Atoi(innerBag[1])
			check(err)
			parsedInnerBags = append(parsedInnerBags, BagContents{count, innerBag[2]})
		}
		parsedBags = append(parsedBags, Bag{Type: bagAndContents[0], Contents: parsedInnerBags})
	}
	return parsedBags
}

func buildGraph(bags Bags, direction string) *graph.Mutable {
	totalBags := len(bags)
	g := graph.New(totalBags)
	for i, bag := range bags {
		for _, innerBag := range bag.Contents {
			if direction == "up" {
				// Inner bag index has an edge to the containing bag index so we can trace backwards from the starting bag
				g.AddCost(bags.FindIndex(innerBag.Type), i, int64(innerBag.Count))
			} else if direction == "down" {
				// Bag has an edge to the inner bags so we can traverse down
				g.AddCost(i, bags.FindIndex(innerBag.Type), int64(innerBag.Count))
			}
		}
	}
	return g
}

func findAllContainingBags(g graph.Iterator, startIndex int) []int {
	var containingIndices []int
	graph.BFS(g, startIndex, func(v, w int, _ int64) {
		containingIndices = append(containingIndices, w)
	})
	return containingIndices
}

func findAllInnerBags(g graph.Iterator, startIndex int) int64 {
	var totalInnerBags int64
	g.Visit(startIndex, func(w int, cost int64) (skip bool) {
		totalInnerBags += cost + cost*findAllInnerBags(g, w)
		return
	})
	return totalInnerBags
}

func Day07() {
	data := readFile("./days/inputs/07.txt")
	bags := parseBags(data)
	startIndex := bags.FindIndex("shiny gold")

	g := buildGraph(bags, "up")
	containingBags := findAllContainingBags(g, startIndex)
	fmt.Println("Total containing bags:", len(containingBags))

	g = buildGraph(bags, "down")
	fmt.Println("Total inner bags:", findAllInnerBags(g, startIndex))
}
