def print_tips(text):
    print('\n>>> ' + text)


def enter_bl_folder():
    print_tips('切换到工作目录')
    os.chdir('bl')
    print(os.getcwd())


def run():
    enter_bl_folder()


if __name__ == '__main__':
    # 暂时用作工作目录
    os.chdir('C:/Users/Administrator/Desktop/iBiliPlayer-bili')
    run()
