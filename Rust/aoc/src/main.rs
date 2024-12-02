mod day0;
mod day1;
mod day2;
mod day3;

use std::fs;

fn main() {
    day0::solve(&*fs::read_to_string("../../inputs/day0.txt").unwrap());
    day1::solve(&*fs::read_to_string("../../inputs/day1.txt").unwrap());
    day2::solve(&*fs::read_to_string("../../inputs/day2.txt").unwrap());
}
