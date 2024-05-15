import {defineStore} from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => ({
        // 定义多个成员变量
        accountName: null,
        accountType: null,
        // 可以继续添加更多的状态变量
    }),
    getters: {
        // 定义 getter，用于从 state 中派生出一些状态
        fullName(state) {
            return `${state.accountName} ${state.accountType}`;
        },
        // 继续定义其他 getters
    },
    actions: {
        // 任何组件挂载都运行这次根据userInfo的赋值
        updateAllData() {
            //最上层路由确保userStore值有效
            if (this.accountType == null && localStorage.getItem('userInfo') !== null) {
                let userInfo = JSON.parse(localStorage.getItem('userInfo'))
                // 给userStore全局变量赋值
                this.updateAccountName(userInfo.accountName)
                this.updateAccountType(userInfo.accountType)
            }
        },
        // 所有值设为null值
        setNullAllData(){
            this.accountName = null
            this.accountType = null
        },
        // 定义 actions，用于修改 state
        updateAccountName(newAccountName) {
            this.accountName = newAccountName;
        },
        updateAccountType(newAccountType) {
            this.accountType = newAccountType;
        },
        // 继续定义其他 actions
    }
});

