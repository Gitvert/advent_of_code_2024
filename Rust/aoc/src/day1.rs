pub(crate) fn solve(input: &str) {
    println!();

    let mut left_list: Vec<i32> = Vec::new();
    let mut right_list: Vec<i32> = Vec::new();

    for row in input.lines() {
        let mut split_iter = row.split("   ");

        let lhs = split_iter.next().unwrap();
        let rhs = split_iter.next().unwrap();

        left_list.push(lhs.parse::<i32>().unwrap());
        right_list.push(rhs.parse::<i32>().unwrap());
    }

    left_list.sort();
    right_list.sort();
    
    solve_part_1(&left_list, &right_list);
    solve_part_2(&left_list, &right_list);
}

fn solve_part_1(left_list: &Vec<i32>, right_list: &Vec<i32>) {
    let mut sum = 0;
    
    for i in 0..left_list.len() {
        let diff = (left_list[i] - right_list[i]).abs();
        sum += diff;
    }

    println!("Day 1 part 1: {}", sum);
}

fn solve_part_2(left_list: &Vec<i32>, right_list: &Vec<i32>) {
    let mut sum = 0;
    
    for left_item in left_list {
        let mut count = 0;
        for right_item in right_list {
            if left_item == right_item {
                count += 1;
            }
        }
        sum += left_item * count;
    }
    
    println!("Day 1 part 2: {}", sum);
}