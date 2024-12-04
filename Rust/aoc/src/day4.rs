use std::collections::HashMap;
use once_cell::sync::Lazy;

static NEXT_LETTER: Lazy<HashMap<char, char>> = Lazy::new(|| {
    let mut map = HashMap::new();
    map.insert('M', 'A');
    map.insert('A', 'S');
    map.insert('S', '!');
    map
});

static mut SIZE: usize = 0;

pub(crate) fn solve(input: &str) {
     println!();

     let rows: Vec<Vec<char>> = input
     .lines()
     .map(|line| line.chars().collect())
     .collect();
    
    solve_part_1(&rows);
    solve_part_2(&rows);
}

fn solve_part_1(rows: &Vec<Vec<char>>) {

    unsafe {
        SIZE = rows.len();
    
        let mut sum = 0;

        for y in 0..SIZE {
            for x in 0..SIZE {
                if rows[y][x] == 'X' {
                    sum += find_no_of_xmas(&rows, y as i32, x as i32, SIZE as i32);
                }
            }
        }

        println!("Day 4 part 1: {}", sum);
    }
}

fn find_no_of_xmas(rows: &Vec<Vec<char>>, y: i32, x: i32, size: i32) -> i32 {
    return find_dir(rows, 'M', y, x, size, (-1, 0))
        + find_dir(rows, 'M', y, x, size, (1, 0))
        + find_dir(rows, 'M', y, x, size, (0, -1))
        + find_dir(rows, 'M', y, x, size, (0, 1))
        + find_dir(rows, 'M', y, x, size, (1, 1))
        + find_dir(rows, 'M', y, x, size, (-1, -1))
        + find_dir(rows, 'M', y, x, size, (-1, 1))
        + find_dir(rows, 'M', y, x, size, (1, -1));
}

fn find_dir(rows: &Vec<Vec<char>>,letter: char, y: i32, x: i32, size: i32, direction: (i32, i32)) -> i32 {
    if letter == '!' {
        return 1;
    }

    if x == 0 && direction.0 == -1 {
        return 0;
    }

    if x == size -1 && direction.0 == 1 {
        return 0;
    }

    if y == 0 && direction.1 == -1 {
        return 0;
    }

    if y == size -1 && direction.1 == 1 {
        return 0;
    }

    if rows[(y + direction.1) as usize][(x + direction.0) as usize] == letter {
        return find_dir(rows, *NEXT_LETTER.get(&letter).unwrap(), y + direction.1, x + direction.0, size, direction);
    } else {
        return 0;
    }
}


fn solve_part_2(rows: &Vec<Vec<char>>) {
    unsafe {
        SIZE = rows.len();
    
        let mut sum = 0;

        for y in 0..SIZE {
            for x in 0..SIZE {
                if rows[y][x] == 'A' {
                    sum += find_no_of_x_mas(&rows, y as i32, x as i32, SIZE as i32);
                }
            }
        }

        println!("Day 4 part 2: {}", sum);
    }
}

fn find_no_of_x_mas(rows: &Vec<Vec<char>>, y: i32, x: i32, size: i32) -> i32 {

    if x < 1 || x == size - 1 || y < 1 || y == size -1 {
        return 0;
    }

    if rows[(y - 1) as usize][(x - 1) as usize] == 'M'
        && rows[(y + 1) as usize][(x - 1) as usize] == 'M'
        && rows[(y + 1) as usize][(x + 1) as usize] == 'S'
        && rows[(y - 1) as usize][(x + 1) as usize] == 'S' {
            return 1;
    }

    if rows[(y - 1) as usize][(x - 1) as usize] == 'M'
    && rows[(y + 1) as usize][(x - 1) as usize] == 'S'
    && rows[(y + 1) as usize][(x + 1) as usize] == 'S'
    && rows[(y - 1) as usize][(x + 1) as usize] == 'M' {
        return 1;
    }

    if rows[(y - 1) as usize][(x - 1) as usize] == 'S'
    && rows[(y + 1) as usize][(x - 1) as usize] == 'S'
    && rows[(y + 1) as usize][(x + 1) as usize] == 'M'
    && rows[(y - 1) as usize][(x + 1) as usize] == 'M' {
        return 1;
    }

    if rows[(y - 1) as usize][(x - 1) as usize] == 'S'
    && rows[(y + 1) as usize][(x - 1) as usize] == 'M'
    && rows[(y + 1) as usize][(x + 1) as usize] == 'M'
    && rows[(y - 1) as usize][(x + 1) as usize] == 'S' {
        return 1;
    }


    0
}