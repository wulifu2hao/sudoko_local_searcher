from subprocess import Popen, PIPE, STDOUT

file_list = ['sudoku-eg1.txt','sudoku-eg2.txt','sudoku-eg3.txt','sudoku-eg4.txt','sudoku-eg5.txt','sudoku-eg6.txt','sudoku-easy1.txt','sudoku-easy2.txt','sudoku-easy3.txt','sudoku-medium1.txt','sudoku-medium2.txt','sudoku-medium3.txt','sudoku-hard1.txt','sudoku-hard2.txt','sudoku-hard3.txt','sudoku-evil1.txt','sudoku-evil2.txt','sudoku-evil3.txt']

for filename in file_list:
    file = open(filename, 'r')
    contents = file.read()

    p = Popen(['java', 'Main'], stdout=PIPE, stdin=PIPE, stderr=STDOUT)
    grep_stdout = p.communicate(input=contents)[0]
    print(filename)
    print(grep_stdout.decode())
