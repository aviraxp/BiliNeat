class HookParam(object):
    online_helper_class = 'NotFound'
    category_method = 'NotFound'
    toolbar_method = 'NotFound'
    drawer_method = 'NotFound'
    found_method = 'NotFound'
    game_center_method = 'NotFound'

    theme_class = 'NotFound'
    theme_param_class = 'NotFound'

    bmall_class = 'NotFound'


def print_tips(text):
    print('\n>>> ' + text)


def enter_bl_folder():
    print_tips('Enter Working Directory')
    os.chdir('bl')
    print(os.getcwd())


def print_result(param):
    print('OnlineHelper    = ' + param.online_helper_class)
    print('isShowCategory  = ' + param.category_method)
    print('isShowToolBar   = ' + param.toolbar_method)
    print('isShowDraw      = ' + param.drawer_method)
    print('isShowFound     = ' + param.found_method)
    print('GameCenter      = ' + param.game_center_method)
    print()
    print('ThemeClass      = ' + param.theme_class)
    print('ThemeParamClass = ' + param.theme_param_class)
    print()
    print('BMallClass      = ' + param.bmall_class)


def find_online_helper(file, param):
    pass


def find_key_text(name, param):
    with open(name, encoding='UTF-8') as file:
        for line in file:
            if 'OnlineParamsHelper' in line:
                param.online_helper_class = name
                find_online_helper(file, param)
            elif r'\u8be5\u76ae\u80a4\u4e0d\u5b58\u5728' in line:
                param.theme_class = name
                file.seek(0)
                methods = re.findall('.method private a\(Lbl/([a-zA-z]{3});\)V', file.read())
                param.theme_param_class = methods[0]


def find_files():
    print_tips('Finding...')
    param = HookParam()
    walk_dir = os.walk(os.curdir)
    exit()

    for root, dirs, files in walk_dir:
        for name in files:
            find_key_text(name, param)

    print_tips('Find Complete')
    print_result(param)


def run():
    enter_bl_folder()
    find_files()


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
