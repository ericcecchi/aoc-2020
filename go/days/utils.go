package days

import "io/ioutil"

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func readFile(s string) string {
	data, err := ioutil.ReadFile(s)
	check(err)
	return string(data)
}

type set struct {
	m map[string]struct{}
}

var exists = struct{}{}

func NewSet() *set {
	s := &set{}
	s.m = make(map[string]struct{})
	return s
}

func (s *set) Add(value string) {
	s.m[value] = exists
}

func (s *set) Remove(value string) {
	delete(s.m, value)
}

func (s *set) Contains(value string) bool {
	_, c := s.m[value]
	return c
}
