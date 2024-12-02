pub(crate) fn solve(input: &str) {
    println!();
    
    solve_part_1(input);
    solve_part_2(input);
}

fn solve_part_1(input: &str) {
    let mut safe_reports = 0;
    
    for line in input.lines() {
        let report: Vec<i32> = line.split_whitespace().map(|l| l.parse::<i32>().unwrap()).collect();
        if is_report_safe(&report) {
            safe_reports += 1;
        }
    }
    
    println!("Day 2 part 1: {}", safe_reports);
    
}

fn solve_part_2(input: &str) {
    let mut safe_reports = 0;

    for line in input.lines() {
        let report: Vec<i32> = line.split_whitespace().map(|l| l.parse::<i32>().unwrap()).collect();

        if is_report_safe(&report) {
            safe_reports += 1;
            continue;
        }
        
        for i in 0..report.len() {
            let modified_report: Vec<i32> = report
                .iter()
                .enumerate()
                .filter(|&(n, _)| n != i)
                .map(|(_, &x)| x)
                .collect();
            
            if is_report_safe(&modified_report) {
                safe_reports += 1;
                break;
            }
        }
    }
    
    println!("Day 2 part 2: {}", safe_reports);
}

fn is_report_safe(report: &Vec<i32>) -> bool {
    let mut distance_list: Vec<i32> = Vec::new();
    
    for i in 0..report.len() - 1 {
        distance_list.push(report[i+1] - report[i]);
    }

    if distance_list.iter().all(|&d| d > 0) || distance_list.iter().all(|&d| d < 0) {
        if distance_list.iter().all(|d| d.abs() < 4) {
            return true;
        }
    }
    
    false
}