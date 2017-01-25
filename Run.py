def print_tips(text):
    print('\n>>> ' + text)


def enter_bl_folder():
    print_tips('切换到工作目录')
    os.chdir('bl')
    print(os.getcwd())


def run():
    enter_bl_folder()


def show_entrance():
    print()
    print('===================================')
    print('‖                                 ‖')
    print('‖    BiliNeat Adaptive Script     ‖')
    print('‖           Author:iAcn           ‖')
    print('‖                                 ‖')
    print('===================================')


if __name__ == '__main__':
    # 暂时用作工作目录
    os.chdir('C:/Users/Administrator/Desktop/iBiliPlayer-bili')
    show_entrance()
    run()
