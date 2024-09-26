# Word guessing game in python

import random
from wordlist import words


#dictionary of key:()
hearts_art =  {0:("💓 💓 💓 💓 💓 💓"),
               1:("💓 💓 💓 💓 💓"),
               2:("💓 💓 💓 💓"),
               3:("💓 💓 💓"),
               4:("💓 💓"),
               5:("💓"),
               6:(" ")}

def display_heart(wrong_guesses):
    for line in hearts_art[wrong_guesses]:
        print(line, end= " ")

def display_hint(hint):
    print(" ".join(hint))

def display_answer(answer):
    pass

def main():
    answer = random.choice(words)
    hint = ["_"] * len(answer)
    wrong_guesses = 0
    guessed_letters = set()
    is_running = True

    while is_running:
        display_heart(wrong_guesses)
        display_hint(hint)
        guess = input("Enter a letter: ").lower()

        if len(guess) != 1 or not guess.isalpha():
            print("Invalid input")
            continue
        
        if guess in guessed_letters:
            print(f"{guess} is already guessed")
            continue

        guessed_letters.add(guess)

        if guess in answer:
            for i in range(len(answer)):
                if answer[i] == guess:
                    hint[i] = guess
        else:
            wrong_guesses += 1 

        if "_" not in hint:
            display_heart(wrong_guesses)
            display_answer(answer)
            print("YOU WIN!")
            is_running = False   
        elif wrong_guesses >= len(hearts_art) - 1:
            display_heart(wrong_guesses)
            display_answer(answer)
            print("YOU LOSE")
            is_running = False      

if __name__ == '__main__':
    main()

