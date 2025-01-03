[TOC]

# 

# Modes

1.   Normal

     Move Cursor, screen

     Change file

     Switch to other mode

2.   Insert/Replace

     Text editing (only the input and delete is valid, cannot change the cursor position)

3.   Visual

     Select text -> visualize it

4.   Command

     Interact with Vim

# Text Operations

## Movement 

```bash
1. cursor level
h, j, k, l 
#left, down, up, right

2. word level
w, b, e, ge 
# w, e is moving to next; b and ge is moving backward
#w move to the next start; e move to the next start
#ge back to the previous word end, b move to the beginning

3. line level
0, $ 
#move to the beginning or end of the current line
0 vs ^ 
# o move to the beginning of the line (include the whitespace); ^ move to the first none-blank character

4. paragraph level/parentheses level
{,}
# jump to the previous/next paragraph
%
# jump to other parentheses

4. page level
ctrl + u or d
# move half page up or down
ctrl + b or f
# move whole page backward or forward

5. file level
gg G # move to the bottom or the end of the file
[number] gg, G # jump to line
```

## Text Editing

```shell
i I # insert mode
# i insert at the current position, I insert at ^
a A # append
# a append at the current position, A insert at $
r R # replace
# r replace the current character, R enters the replace mode
# [Replace mode] delete results in undo the replacement

o O # insert a new line below or above this line

u # undo
ctrl + r # redo
. # repeat the last change

d D # deletion
c C # change mode (delete + insert)

x X #delete the current word

<<, >>, == # indent change
# del or insert or auto indent

y # copy the selected text
yy, Y # copy the whole line
-> p, P # paste the line above or below
yy$ or yy0 or yy^ #cursor to the position

p, P # paste after or before the cursor
gp, gP # paste and move the cursor after
```

Deletion Details (c -> change mode: do the deletion and then enter the insert mode)

```shell
d[movement] or c[movement]
d # the whole line
$ # the rest of the line
0 # the beginning till current position
w # to the beginning of the next word
b # to the beginning of the current word
h, j, k, l # left, down, up, right
[n] [movement]
```



## Text Searching and Replacing

```bash
1. search below
/[text] + n N # search text and then move to the next find or the previous find
2. search above
?[text]

1. replace entire occurance in entire file
%s/[old]/[new]/g
2. replace with confirmation
%s/[old]/[new]/gc
[options]
y, n, a[this and the remainings], q[quit], 

:5,10s/old/new/g   - replace in lines 5-10
:.,$s/old/new/g    - replace from current line to end
:'<,'>s/old/new/g  - replace in visual selection

char seach
f[char] #move to the next char in the current line
F[char] #move to the previous char in the current line
; , # repeat the last F or f
```



## Visual mode (select text)

### Enter visual mode

```bash
v
# select @ character level
V
# select @ line level
ctrl + v
# select @ block level
```

### Navigation options

```bash
h, j, k, l # move the cursor left, down, up, and right
w, b # move the selction forward/backward on word level
0, $ # line start or line end
gg, G # file start, end
```

### Operations

```bash
d # delete the selected text
y # yank the selected text
c # change the selected text
> # indent right
< # indent left
~ # change case (First letter uppercase and the left is lowercase)
u # change to lower case
U # change to upper case
```

## Special Operations

```bash
vi[delimiter] # select inside the delimiter
di[delimiter]
ci[delimiter]

va[delimiter] # select include the delimiter
da[delimiter]
ca[delimiter]

[exapmples]
viw - inner word
vaw - around word
vi" - inner quotes
va" - around quotes
vi' - inner single quotes
va' - around single quotes
vi( - inner parentheses
va( - around parentheses
vi{ - inner curly braces
va{ - around curly braces
vi[ - inner square brackets
va[ - around square brackets
vit - inner HTML/XML tag
vat - around HTML/XML tag
```



# Command Mode

```bash
:![command]
# external command

:sh
# open a shell

[n][actions/movements] 
# do the action n times
```

## File Operations

### Open Command

```bash
:e [filename] -> open multiple buffers at a time
```

### Quit or Save Command

```bash
:q or ZQ # quit (only nothing changed)
:q! # quit and disgard all changes
:wq  or ZZ # quit and save all changes
:x #same as :wq

:w # just save and not quit
:w [filename] # save as a new file
:wq! # force save and quit
```

## Buffer Operations 

>   **What is a buffer**
>
>   -   It's a temporary holding space for text
>
>   -   Each file you open creates a new buffer
>
>   -   You can have multiple buffers open at once

```bash
:ls
# list all the buffers

:b [number]
# switch buffer by number
:bn or bp
# switch to next buffer or previous buffer

:bd [number] (2,5 or 2-5)
# close the buffer with number
:bw [number]
# save + close the buffer with number
```

## Window Operations

### Window Split

```bash
vim -o/-O [path_1] [path_2]
# outside vim

:sp, :vsp [filename]
# split and open file

:only display one window only
:q close current window
```

### Navigate Cursor among window

```bash
Ctrl + w + h, j, k, l
```

