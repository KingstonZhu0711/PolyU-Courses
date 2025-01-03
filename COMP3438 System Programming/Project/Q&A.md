```C
#include <linux/module.h>
#include <linux/init.h>
#include <linux/fs.h>
#include <linux/types.h>
#include <linux/printk.h>
#include <linux/kdev_t.h>
#include <linux/cdev.h>
#include <linux/string.h>
#include <asm/uaccess.h>

// starting number of minor number
#define S_N 1
// number of minor number (number of device)
#define N_D 2
#define DEVICE_NAME "helloworld char driver"


static char msg[] = "Hello World!!!";
// dev_v is __u32: unsigned 32 bit, and this is a global variable
// with out extern keyword -> this is a definition(alloc 32 bits)
// It store both the major and minor number: top 12 bit for major, 20 bit for minor
static dev_t devno;
// cdev is a struct, one attribute is file_operations, one is dev_t
static struct cdev mydev;


/**
 * in user space: int fd = open("/dev/helloworld", O_RDONLY);
 * inode contains device info from /dev/helloworld
 * fp is the kernel's file structure for this opened instance
 * The user's fd (like 3) maps to this fp in kernel space
 */
static int myopen(struct inode *inode, struct file *fp) {
    printk("Device " DEVICE_NAME " opened.\n");
    return 0;
}

/**
 * in user space: char buffer[100];
 *				  read(fd, buffer, sizeof(buffer));
 * fp is the kernel's file structure for this opened instance
 * The user's fd (like 3) maps to this fp in kernel space
 * the user space pointer buffer = buf
 * count means the space offered by user
 * position useless here
 */
static ssize_t myread(struct file *fp, char __user *buf, size_t count, loff_t *position) {
    int num;
    int ret;
    if (count < strlen(msg)) {
        num = count;
    } else {
        // another strlen in <linux/string.h>
        num = strlen(msg);
    }
    // let the kernel pointer and user pointer talking to each other
    ret = copy_to_user(buf, msg, num); // (dst, src, num)
    if (ret) { //non-zero means failure -> if condition is true
        printk("Fail to copy data from the kernel space to the user space.\n");
    }
    return num;
}

static int myclose(struct inode *inode, struct file *fp) {
    printk("Device " DEVICE_NAME " closed.\n");
    return 0;
}

// a collection of function pointers specify the available operations for the user and how to do
// kind of function overwritting
static struct file_operations myfops = {
    owner: THIS_MODULE, 
    open: myopen, 
    read: myread, 
    release: myclose 
};

// __init is a macro: telling compiler to put this function to the .init.text section (default is .text section) of the assembly code so that the kernel could find it
static int __init helloworldinit(void) {
    int ret;
    // register a major number 
    /**
     * Usage: int alloc_chrdev_region(dev_t *dev, unsigned baseminor, 
     			unsigned count, const char *name)
     			(pointer points to name is const)
     * Explaination:
     * alloc_chrdev_region() - register a range of char device numbers
     * @dev: output parameter for first assigned number
     * @baseminor: first of the requested range of minor numbers
     * @count: the number of minor numbers required
     * @name: the name of the associated device or driver
     *
     * Allocates a range of char device numbers.  The major number will be
     * chosen dynamically, and returned (along with the first minor number)
     * in @dev.  Returns zero or a negative error code.
     */
    ret = alloc_chrdev_region(&devno, S_N, N_D, DEVICE_NAME);
    if (ret < 0) {
        // inside kernel memory space, you cannot use perror or printf
        // usage is differnt, it will concate automatically!
        printk("failure" DEVICE_NAME " cannot get major number.\n");
        return ret;
    }
    int major = MAJOR(devno);
    printk("Device " DEVICE_NAME " initiailized (major number = %d).\n", major);
    // register a char device 
    // init the mydev struct with myfops info
    cdev_init(&mydev, &myfops);
    mydev.owner = THIS_MODULE;
    /**
     * Usage: int cdev_add(struct cdev *p, dev_t dev, unsigned count)
     * Explanation:
     * cdev_add() - add a char device to the system
     * @p: the cdev structure for the device
     * @dev: the first device number for which this device is responsible
     * @count: the number of consecutive minor numbers corresponding to this
     *         device
     *
     * cdev_add() adds the device represented by @p to the system, making it
     * live immediately.  A negative error code is returned on failure.
     */
    ret = cdev_add(&mydev, devno, N_D);
    if (ret) {
        printk("Device " DEVICE_NAME " register fail.\n");
        return ret;
    }
    return 0;
}

# __exit is a macro: telling compiler to put this function to the .exit.text section (default is .text section) of the assembly code so that the kernel could find it
static void __exit helloworldexit(void) {
    // delete the row of the Char device table
    cdev_del(&mydev);
    // unrigester the major and minor number and free space
    unregister_chrdev_region(devno, N_D);
    printk("Device " DEVICE_NAME " unloaded.\n");
}

/**
 * Usage: #define module_init(x)	__initcall(x);
 * Explanation:
 * module_init() - driver initialization entry point
 * @x: function to be run at kernel boot time or module insertion
 * 
 * module_init() will either be called during do_initcalls() (if
 * builtin, driver is a part of the kernal) or at module insertion time (if a module, can be
 * loaded into the kernel).  There can only be one per module.
 * 
 */
module_init(helloworldinit);

/**
 * Usage: #define module_exit(x)	__exitcall(x);
 * Explanation:
 * module_exit() - driver exit entry point
 * @x: function to be run when driver is removed
 * 
 * module_exit() will wrap the driver clean-up code
 * with cleanup_module() when used with rmmod when
 * the driver is a module.  If the driver is statically
 * compiled into the kernel, module_exit() has no effect.
 * There can only be one per module.
 */
module_exit(helloworldexit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Qixin Wang");
MODULE_DESCRIPTION("Hello world character device driver");

```

1.   Most of the kernels are interrupt handlers. System Calls are software interrupts. The interrupt routines are so-called drivers. The lookup table is where the driver programs residence and needed to be registered here (`insmod`)

2.   Kconfig software helps to customize how to compile the files

3.   `static` keyword for identifiers (function or global variable) -> this thing is only visible for this file (some kind of encapsulation)

4.   Declaration of a variable: know the name + type

     Definition of a variable: alloc the memory for it

     Declaration of a function: know signature (name + return type + parameter list)

     Definition of a variable: signature + function body (alloc program memory)

5.   `extern` for a global variable: it is declared but not defined

     Without `extern` -> the global variable is defined

6.   Major number -> id of the driver

     Minor number -> parameter used by the driver program to distinguish the hardware instances that share this driver.

     1 major number could be mapped to multiple minor numbers

     (Major number, Minor number) -> specify a device file

7.   Register a char device?

     >1.   Character device driver table: rows are indexed by the major number. columns are indexed by the system call function name.
     >2.   if the user calls a character device open system call, then the kernel will search for the character device driver table based on the device file that the user is calling from the device files. I know it will get the major number.
     >3.   Based on the major number, search for the columns labeled open and from that open column of the major row, it will find a function pointer.
     >4.   So setting up these function pointers in the character device driver table, that's what the register a character device means.

     

```C
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

void main() {
    int fd; 
    char buf[1024];
    // calls -> myopen
    fd = open("/dev/helloworld", O_RDONLY);
    if (fd < 0) {
        perror("Open /dev/helloworld failure.");
        exit(EXIT_FAILURE);
    }
    // calls -> myread
    int num = read(fd, buf, sizeof(buf)-1);
    buf[num] = 0;
    printf("Got the message from /dev/helloworld: \"%s\".\n", buf);
    // calls -> myclose
    close(fd);
    exit(EXIT_SUCCESS);
}

```

