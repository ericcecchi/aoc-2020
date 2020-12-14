PasswordAndPolicy = tuple[int, int, str, str]


def parse_data(data: str) -> list[PasswordAndPolicy]:
    lines = data.split("\n")
    parsed = []
    for line in lines:
        if not line:
            continue
        policy, password = line.split(': ')
        occurrences, letter = policy.split(' ')
        min_val, max_val = map(lambda val: int(val), occurrences.split('-'))
        parsed.append((min_val, max_val, letter, password))

    return parsed


def validate1(data: PasswordAndPolicy) -> bool:
    letter_count = 0
    min_val, max_val, letter, password = data
    for char in password:
        if char == letter:
            letter_count += 1

    return min_val <= letter_count <= max_val


def validate2(data: PasswordAndPolicy) -> bool:
    min_val, max_val, letter, password = data

    return (password[min_val - 1] == letter and password[max_val - 1] != letter) or (
                password[min_val - 1] != letter and password[max_val - 1] == letter)


def part1(data: str):
    parsed = parse_data(data)
    valid_items = 0
    for item in parsed:
        is_valid = validate1(item)
        if is_valid:
            valid_items += 1

    print(f"Total passwords: {len(parsed)}")
    print(f"Valid passwords: {valid_items}")


def part2(data: str):
    parsed = parse_data(data)
    valid_items = 0
    for item in parsed:
        is_valid = validate2(item)
        if is_valid:
            valid_items += 1

    print(f"Total passwords: {len(parsed)}")
    print(f"Valid passwords: {valid_items}")
