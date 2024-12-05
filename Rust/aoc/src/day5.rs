pub(crate) fn solve(input: &str) {
    println!();

    let ordering_rules: Vec<(i32, i32)> = input.lines()
        .filter(|line| line.contains("|"))
        .map(|line| {
            let parts: Vec<i32> = line.split('|').map(|s| s.parse().unwrap()).collect();
            (parts[0], parts[1])
        })
        .collect();

    let update_page_numbers: Vec<Vec<i32>> = input.lines()
        .filter(|line| line.contains(','))
        .map(|line| {
            line
                .split(',')
                .map(|s| s.parse().unwrap())
                .collect()
        })
        .collect();
    
    solve_part_1(&ordering_rules, &update_page_numbers);
    solve_part_2(&ordering_rules, &update_page_numbers);
}

fn solve_part_1(ordering_rules: &Vec<(i32, i32)>, update_page_numbers: &Vec<Vec<i32>>) {
    let sum = update_page_numbers.iter()
        .filter(|page_numbers| update_has_correct_order(page_numbers, &ordering_rules))
        .map(|page_numbers| find_middle_number(page_numbers))
        .reduce(|acc: i32, n| acc + n)
        .unwrap();

    println!("Day 5 part 1: {}", sum);
}

fn solve_part_2(ordering_rules: &Vec<(i32, i32)>, update_page_numbers: &Vec<Vec<i32>>) {

    let sum = update_page_numbers.iter()
        .filter(|page_numbers| !update_has_correct_order(page_numbers, &ordering_rules))
        .map(|page_numbers| change_order(page_numbers, &ordering_rules))
        .map(|page_numbers| find_middle_number(&page_numbers))
        .reduce(|acc: i32, n| acc + n)
        .unwrap();

    println!("Day 5 part 2: {}", sum);
}

fn update_has_correct_order(page_numbers: &Vec<i32>, ordering_rules: &Vec<(i32, i32)>) -> bool {
    for i in 0..page_numbers.len()-1 {
        for j in i+1..page_numbers.len() {
            let tuple: (i32, i32) = (page_numbers[i], page_numbers[j]);

            if !ordering_rules.contains(&tuple) {
                return false;
            }
        }
    }

    return true;
}

fn find_middle_number(page_numbers: &Vec<i32>) -> i32 {
    return page_numbers[(page_numbers.len() - 1) / 2];
}

fn change_order(page_numbers: &Vec<i32>, ordering_rules: &Vec<(i32, i32)>) -> Vec<i32> {
    let mut fixed_page_numbers = page_numbers.clone();

    for i in 0..fixed_page_numbers.len()-1 {
        for j in i+1..fixed_page_numbers.len() {
            let tuple: (i32, i32) = (fixed_page_numbers[i], fixed_page_numbers[j]);

            if !ordering_rules.contains(&tuple) {
                fixed_page_numbers.swap(i, j);
            }
        }
    }

    return fixed_page_numbers;
}