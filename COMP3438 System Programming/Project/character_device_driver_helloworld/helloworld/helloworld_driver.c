#include <linux/module.h>
#include <linux/init.h>
#include <linux/fs.h>
#include <linux/types.h>
#include <linux/printk.h>
#include <linux/kdev_t.h>
#include <linux/cdev.h>
#include <linux/string.h>
#include <asm/uaccess.h>

#define S_N 1
#define N_D 2
#define DEVICE_NAME "helloworld char driver"


static char msg[] = "Hello World!!!";
static dev_t devno;
static struct cdev mydev;

static int myopen(struct inode *inode, struct file *fp) {
    printk("Device " DEVICE_NAME " opened.\n");
    return 0;
}

static ssize_t myread(struct file *fp, char __user *buf, size_t count, loff_t *position) {
    int num;
    int ret;
    if (count < strlen(msg)) {
        num = count;
    } else {
        num = strlen(msg);
    }
    ret = copy_to_user(buf, msg, num);
    if (ret) {
        printk("Fail to copy data from the kernel space to the user space.\n");
    }
    return num;
}

static int myclose(struct inode *inode, struct file *fp) {
    printk("Device " DEVICE_NAME " closed.\n");
    return 0;
}

static struct file_operations myfops = {
    owner: THIS_MODULE, 
    open: myopen, 
    read: myread, 
    release: myclose 
};


static int __init helloworldinit(void) {
    int ret;
    // register a major number 
    ret = alloc_chrdev_region(&devno, S_N, N_D, DEVICE_NAME);
    if (ret < 0) {
        printk("failure" DEVICE_NAME " cannot get major number.\n");
        return ret;
    }
    int major = MAJOR(devno);
    printk("Device " DEVICE_NAME " initiailized (major number = %d).\n", 
    major);
    // register a char device 
    cdev_init(&mydev, &myfops);
    mydev.owner = THIS_MODULE;
    ret = cdev_add(&mydev, devno, N_D);
    if (ret) {
        printk("Device " DEVICE_NAME " register fail.\n");
        return ret;
    }
    return 0;
}

static void __exit helloworldexit(void) {
    cdev_del(&mydev);
    unregister_chrdev_region(devno, N_D);
    printk("Device " DEVICE_NAME " unloaded.\n");
}

module_init(helloworldinit);
module_exit(helloworldexit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Qixin Wang");
MODULE_DESCRIPTION("Hello world character device driver");
