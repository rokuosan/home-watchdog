import sys
import gpt4free as g


def ask_to_ai(prompt: str) -> str:
    response = g.Completion.create(g.Provider.You, prompt=prompt)
    return response


def main():
    args = sys.argv
    prompt: str = rf'{args[1]}'
    ans: str = ask_to_ai(prompt)
    print(ans.encode('utf-8').decode('unicode-escape'), end='')


if __name__ == '__main__':
    main()
