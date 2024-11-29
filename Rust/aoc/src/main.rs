mod day0;

use std::fs;

fn main() {
    day0::solve(&*fs::read_to_string("../../inputs/day0.txt").unwrap());
}
