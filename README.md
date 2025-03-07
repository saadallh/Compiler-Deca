# Compiler from scratch
## GL24 Project - ARM Assembler Extension

**Date:** 01/01/2023  
**Author:** GL24 Team

## Overview

This project includes an extension for running ARM assembler files using a custom script named `run_arm`. The script is designed to execute ARM assembly code on a Linux machine using the QEMU emulator. This extension is currently separate from the main project to avoid interference with the IMA compiler.

## Prerequisites

To use the `run_arm` script, you need the following:

- A Linux machine
- QEMU emulator
- ARM cross-compiler

### Installation

Before running the ARM assembler files, you need to install the required packages. Run the following commands in your terminal:

```bash
sudo apt install gcc-arm-linux-gnueabi
sudo apt install qemu-user
```
### Usage
Once the necessary packages are installed, you can execute an ARM assembler file using the run_arm script. Use the following command:

```bash
./run_arm fichier.ass
```
Replace fichier.ass with the name of your ARM assembler file.

