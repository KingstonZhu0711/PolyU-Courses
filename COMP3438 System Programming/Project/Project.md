# Declaration

>   1.   If you get stuck with one step, you can skip it and demo (or partially demo) the subsequent step(s), so as to get at least some partial scores.
>   2.   If at any point that the screen goes black and argues that there is no mDP signal, you can report and reschedule a time for another exam.

# Procedure

>   1.   You should arrive at the starting time of your group and you and others of your groups will enter the room together.
>
>   2.   You will be assigned by Qixin a PC and do the initializations (see Before Meeting Starts) and you should report any irregularities to Qixin and he will tackle or let you change a PC.
>
>   3.   Then the first guy of your time slot start and others leave out the room and wait.
>
>   4.   You start to demo and explain the six steps and Qixin will start timer.
>
>   5.   Don’t do the demo exceeding 6 mins (timeout), you will get no Q&A part if you timeout and Qixin will let you go.
>
>   6.   If you have any problems need restart the computer during your demo like network and Ubuntu gets stuck, report and Qixin will let you restart or choose another PC and you will go out and assigned to the next group (PC left there to initiate)
>
>   7.   The QA question for day 1 is: “in case you are told to print ‘goodbye’ message every time you unload the module, what should you do?”
>
>        Answer: use vim to open the helloworld_driver.c file and go to `helloworldexit` function and modify the `printk` function and change its contents as “goodbye”. Then you should quit with save (`:wq` or `ZZ` command in the normal mode).

# Before Meeting Starts

>   1.   **Open the preinstalled Ubuntu Linux virtual machine** upon VirtualBox on a PQ603 COMP desktop 
>
>        Step:
>
>        Double click “Apps(Y:)” drive \Subject\vm_image\EmbeddedSystem\EmbeddedSystem_2024 A ==shortcut== file.

>   2.   Open the COMP desktop Windows **PuTTY** to connect to the COMP ARM embedded computer, and set up NFS **remote directory** mounting between the COMP desktop Linux and the COMP ARM embedded computer.
>
>        Step:
>
>        1.   Search and Open PuTTY in Windows.
>        
>        2.   Select `Serial` option, Device: `COM3`, Broad speed: `115200`
>        
>        3.   Press enter in the terminal PuTTY opens
>        
>        4.   `mount -t nfs 192.168.1.1:/home/comp3438/arm-board /mnt/nfs -o nolock`
>        
>             ==This is very important, auxin will restart the arm-board, you cannot use other people’s mount, you have to mount yourself==
>        
>             ==Emergency: If this wait a long time and no response, no solution yet.==
>        
>             ==Emergency: If this says bad fileno, plug in the rightmost cable of the embedded device again.==
>        
>             ==Emergency: Line busy, this shows everything is done, but normally this occurs when you double mount.==

>   3.   qixin do the recording



# Main Steps

>1.   Download files from Blackboard in Linux
>
>    Steps:
>
>    1.   Ctrl + Alt + T open the terminal in Linux
>    
>    2.   Use cursor click the Firefox or `firefox `open the browser
>    
>         `firefox polyubb` directly visit the wrong page and you shouldn’t use this.
>    
>    3.   Search + login Bb and download zip from content/week9/Actual Lecture/Char Device Driver Demo 1 (Hello World) (updated on Nov 5 7:25PM) material
>    
>    4.   `unzip Downloads/helloworld.zip`

>   2.   Move the files to corresponding positions
>
>        Steps:
>
>        1.   `cp helloworld/helloworld_user.c  arm-board`
>        
>        2.   `cp helloworld/helloworld_driver.c helloworld/Kconfig helloworld/Makefile arm-board/linux-3.0.8/drivers/char/` or you can do it separately
>        
>        3.   `cd arm-board`
>        
>        4.   `arm-linux-gcc helloworld_user.c -o helloworld`
>        
>        5.   `cd linux-3.0.8`
>        
>        6.   `make menuconfig`
>        
>             ==don’t use make config command, this will broke the make and you cannot finish the project==
>        
>        7.   Enter the Device driver/Char to the bottom, and find the hello world and tap m, then quit
>        
>        8.   `make`: you shouldn’t speak when compiling

>   3.   Create a module in embedded device and run
>
>        Steps:
>
>        1.   `insmod linux-3.0.8/drivers/char/helloworld_driver.ko`
>        
>             ==Emergency: If this failed “192.168.1.1 no response”, try close the terminal on the Desktop Ubuntu.==
>        
>        2.   `mknod /dev/helloworld c 250 1`
>        
>        3.   `cd (back to the arm-board directory)`
>        
>        4.   `./hellowrld_user`

>4.   Delete the module ==This is a must==
>
>  Steps:
>
>  1.   `rm /dev/helloworld`
>
>  2.   `rmmod helloworld_driver` (no '.ko')
>
>       Below are useless
>
>  3.   `cat /proc/devices` to check I really deleted (name in this file is the i-node name, that is `helloworld`)
>
>  4.   `lsmod | grep he` to see the true module name, that is `helloworld_driver`
>
>       ==Emergency: If the module are empty, just try to restart the embedded device with touch -> settings -> restart or replug the middle usb cable of the embedded device to recover.==

 

# Appendix: rubrics

>   These steps include: 
>
>   1.   download or develop the driver source code in the proper directory;
>   2.   download or develop the the application source code in the proper directory; 
>   3.   driver compilation;
>   4.   driver loading and device file set up;
>   5.   application execution;
>   6.   device file deletion and driver unloading;
>   7.   the student can successfully and correctly complete small changes to satisfy ad hoc change requests proposed in the Q & A section.
