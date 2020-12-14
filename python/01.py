def find_magic_number(num: int, total: int, source: list[str]):
    magic_num = total - int(num)

    if str(magic_num) in source:
        return magic_num


def part1(data: str):
    lines = data.split("\n")

    for line in lines:
        if not line:
            continue
        magic_num = find_magic_number(int(line), 2020, lines)

        if magic_num:
            print(f"Found match! {line}, {magic_num}")
            print(f"Product {int(line) * int(magic_num)}")
            break


def part2(data: str):
    lines = data.split("\n")

    for line in lines:
        if not line:
            continue
        magic_total = 2020 - int(line)

        for nextLine in lines:
            if not nextLine:
                continue
            magic_num = find_magic_number(int(nextLine), magic_total, lines)

            if magic_num:
                print(f"Found match! {line}, {nextLine}, {magic_num}")
                print(f"Product {int(line) * int(nextLine) * int(magic_num)}")
                exit(1)
