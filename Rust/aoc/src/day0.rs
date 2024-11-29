pub(crate) fn solve(input: &str) {
    solve_part_1(input);
    solve_part_2(input);
}

fn solve_part_1(input: &str) {
    let mut sum: u32 = 0;
    
    for row in input.lines() {
        let mut first = 'f';
        let mut last = 'l';
        for character in row.chars() {
            if character.is_digit(10) && first == 'f' {
                first = character;
                last = character;
            } else if character.is_digit(10) {
                last = character;
            }
        }
        
        let calibration_value = first.to_string() + last.to_string().as_str();
        
        sum += calibration_value.parse::<u32>().unwrap();
    };
    
    println!("Day 0 part 1: {}", sum);
}

fn solve_part_2(input: &str) {
    let mut sum: u32 = 0;
    
    for row in input.lines() {
        let mut first = 'f';
        let mut last = 'l';
        
        let row = row
            .replace("one", "o1ne")
            .replace("two", "t2wo")
            .replace("three", "t3hree")
            .replace("four", "f4our")
            .replace("five", "f5ive")
            .replace("six", "s6ix")
            .replace("seven", "s7even")
            .replace("eight", "e8ight")
            .replace("nine", "n9ine");

        for character in row.chars() {
            if character.is_digit(10) && first == 'f' {
                first = character;
                last = character;
            } else if character.is_digit(10) {
                last = character;
            }
        }

        let calibration_value = first.to_string() + last.to_string().as_str();

        sum += calibration_value.parse::<u32>().unwrap();
    }

    println!("Day 0 part 2: {}", sum);
}