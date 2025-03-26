package main
import (
	"math/rand"
	"time"
)

func exec(time_max int) int {

	rand.Seed(42)
	time_sleep := rand.Intn(time_max)
	time.Sleep(time.Duration(time_sleep) * time.Millisecond)
	return time_sleep

}

func aux(max_sleep_ms int) chan int{

	ch := make(chan int)

	go func() {
		for i:=0; i < 1000; i++ {
			ch <- exec(max_sleep_ms)
		}
	}()

	return ch

}

func main() {

	ch1 := aux(20)
	ch2 := aux(200)

	var soma int

	for i:=0; i < 500; i++{
		soma += <- ch1
	}

	for i:=0; i < 500; i++{
		soma += <-  ch2
	}

	print(soma, "\n")
}