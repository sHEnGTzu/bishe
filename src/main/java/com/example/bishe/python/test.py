# test.py
import sys

def add_numbers(a, b):
    return a + b

if __name__ == "__main__":
    if len(sys.argv) == 3:
        num1 = int(sys.argv[1])
        num2 = int(sys.argv[2])
        result = add_numbers(num1, num2)
        print(result)
    else:
        print("Please provide two numbers as arguments.")