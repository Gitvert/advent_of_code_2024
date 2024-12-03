use regex::Regex;

pub(crate) fn solve(input: &str) {
    println!();
    
    solve_part_1(input);
    solve_part_2(input);
}

fn solve_part_1(input: &str) {
    let regex = Regex::new("mul\\(\\d+,\\d+\\)").unwrap();
    
    let multiplications: Vec<String> = regex.find_iter(input)
        .map(|mat| mat.as_str().to_string())
        .collect();
    
    let mut sum = 0;
    let regex = Regex::new("\\d+").unwrap();
    
    for mul in multiplications {
        let numbers: Vec<i32> = regex.find_iter(&*mul)
            .map(|mat| mat.as_str().to_string().parse::<i32>().unwrap())
            .collect();
        
        sum += numbers[0] * numbers[1];
    }
    
    println!("Day 3 part 1: {}", sum);
}

fn solve_part_2(input: &str) {
    let mul_regex = Regex::new("mul\\(\\d+,\\d+\\)").unwrap();
    let dont_regex = Regex::new("don't\\(\\)").unwrap();
    let do_regex = Regex::new("do\\(\\)").unwrap();

    let mut sum = 0;
    let mut do_active = true;
    let mut current_sub_string = input.to_string();

    loop {
        let mul_match_index = mul_regex
            .find(&current_sub_string)
            .map(|mat| mat.start() as i32)
            .unwrap_or(1000000);
        let do_match_index = do_regex
            .find(&current_sub_string)
            .map(|mat| mat.start() as i32)
            .unwrap_or(1000000);
        let dont_match_index = dont_regex
            .find(&current_sub_string)
            .map(|mat| mat.start() as i32)
            .unwrap_or(1000000);
        
        if mul_match_index == 1000000 && do_match_index == 1000000 && dont_match_index == 1000000 {
            break;
        }
        
        if mul_match_index < do_match_index && mul_match_index < dont_match_index {
            let mat = mul_regex.find(&current_sub_string).unwrap().as_str().to_string().replace("mul(", "").replace(")", "").to_string();
            let mut parts = mat.split(",");
            let x = parts.next().unwrap().parse::<i32>().unwrap();
            let y = parts.next().unwrap().parse::<i32>().unwrap();
            
            if do_active {
                sum += x * y;
            }
            
            current_sub_string = (&current_sub_string[(mul_match_index + 1) as usize..current_sub_string.len()]).parse().unwrap()
        } else if do_match_index < mul_match_index && do_match_index < dont_match_index {
            do_active = true;

            current_sub_string = (&current_sub_string[(do_match_index + 1) as usize..current_sub_string.len()]).parse().unwrap()
        } else if dont_match_index < mul_match_index && dont_match_index < do_match_index {
            do_active = false;

            current_sub_string = (&current_sub_string[(dont_match_index + 1) as usize..current_sub_string.len()]).parse().unwrap()
        }
    }

    println!("Day 3 part 2: {}", sum);
}