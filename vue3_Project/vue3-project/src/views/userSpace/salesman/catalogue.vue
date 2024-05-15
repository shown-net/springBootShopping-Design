<template>
  <div>
    <el-table height="450" :data="catalogue" border
              :highlight-current-row="true" :row-style="{height:'100px'}">
      <el-table-column width="40px" type="selection"></el-table-column>
      <el-table-column label="商品信息" prop="name">
        <template #default="{row}">
          <el-image :src=(baseImageUrl+row.imageUrl) alt="" class="img_Product"></el-image>
          <el-input minlength="1" maxlength="50" show-word-limit v-model="row.name"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="商品种类" prop="kind">
        <template #default="{row}">
          <el-select v-model="row.kind" placeholder="请选择商品种类">
            <el-option label="笔记本电脑" value="笔记本电脑"></el-option>
            <el-option label="台式电脑" value="台式电脑"></el-option>
            <el-option label="平板电脑" value="平板电脑"></el-option>
            <el-option label="手机通信" value="手机通信"></el-option>
            <el-option label="智能产品" value="智能产品"></el-option>
            <el-option label="配件办公" value="配件办公"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="单价(￥)" prop="price">
        <template #default="{row}">
          <el-input-number v-model="row.price"></el-input-number>
        </template>
      </el-table-column>
      <el-table-column label="操作" prop="id">
        <template #default="{row}">
          <el-button @click="updateProduct(row)" style="margin: auto 50px auto auto">更新</el-button>
          <el-popconfirm title="确定要删除该商品吗？" @confirm="deleteGoods(row.id,row.imageUrl)">
            <template #reference>
              <el-button type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-button type="text" @click="dialogFormVisible = true">添加新商品</el-button>
    <el-dialog title="新商品信息" v-model="dialogFormVisible">
      <el-form :model="productForm">
        <el-form-item label="商品种类" :label-width="formLabelWidth">
          <el-select v-model="productForm.kind" placeholder="请选择合适种类">
            <el-option label="笔记本电脑" value="笔记本电脑"></el-option>
            <el-option label="台式电脑" value="台式电脑"></el-option>
            <el-option label="平板电脑" value="平板电脑"></el-option>
            <el-option label="手机通信" value="手机通信"></el-option>
            <el-option label="智能产品" value="智能产品"></el-option>
            <el-option label="配件办公" value="配件办公"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称" :label-width="formLabelWidth">
          <el-input v-model="productForm.name" minlength="1" maxlength="50" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="单价(￥)" :label-width="formLabelWidth">
          <el-input-number min="0" v-model="productForm.price"></el-input-number>
        </el-form-item>
        <el-form-item label="数量(个)" :label-width="formLabelWidth">
          <el-input-number min="0" v-model="productForm.quantity"></el-input-number>
        </el-form-item>
        <el-form-item label="销售状态" :label-width="formLabelWidth">
          <el-select v-model="productForm.state" placeholder="请选择合适状态">
            <el-option label="待销售" value="待销售"></el-option>
            <el-option label="有货" value="有货"></el-option>
            <el-option label="无货" value="无货"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品图片" :label-width="formLabelWidth">
          <el-upload
              class="upload-demo"
              drag
              ref="uploadRef"
              :action="baseImgUploadUrl"
              :limit="1"
              :auto-upload="false"
              @success="handleSuccess"
              @exceed="handleExceed">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addGoods()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>

import instance from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {onMounted, reactive, ref} from "vue";
import {useUserStore} from "@/utils/store.js";

export default {
  setup() {
    const userStore = useUserStore();
    onMounted(() => {
          instance.post("/product/getInfoBySalesMan", [])
              .then(result => {
                catalogue.value = result.data
              })
        }
    )
    const catalogue = ref([])   // 商品目录
    // 商品图片源地址
    const baseImageUrl = import.meta.env.VITE_APP_BASE_URL + '/images/download/'
    //当地时间
    const currentTime = ref(null)
    const productForm = reactive({
      kind: '',
      name: '',
      price: 0,
      quantity: 0,
      imageUrl: '',
      state: null
    })
    // 对话框可见标志
    const dialogFormVisible = ref(false)
    const formLabelWidth = ref("120px")
    // 图片上传源网址
    const baseImgUploadUrl = import.meta.env.VITE_APP_BASE_URL + "/images/upload"
    const uploadRef = ref(null);
    // 从商品目录删除某个商品
    const deleteGoods = (ID, imageUrl) => {
      catalogue.value = catalogue.value.filter(item => item.id !== ID);
      instance.post("/product/delete", ID);
      instance.post("/images/delete", imageUrl)
      ElMessage.success("删除成功")
    }
    // 获取上传的图片文件名 response为Result类对象,并且执行插入商品到数据库操作
    const handleSuccess = (result) => {
      if (result.code !== 0) {
        ElMessage.error(result.message)
      } else {
        productForm.imageUrl = result.data
        catalogue.value.push({
          'kind': productForm.kind,
          'name': productForm.name,
          'price': productForm.price,
          'quantity': productForm.quantity,
          "imageUrl": productForm.imageUrl,
          'state': productForm.state
        });
        instance.post("/product/add", productForm);
        dialogFormVisible.value = false
        ElMessage.success("添加成功")
      }
    }
    // 处理文件个数超出限制
    const handleExceed = () => {
      ElMessage.error("最多只能上传一张商品图片")
    }
    // 添加新商品
    const addGoods = () => {
      // 触发上传文件事件
      if (uploadRef.value) {
        uploadRef.value.submit();
      }
    }
    // 更新单个商品信息
    const updateProduct = (project) => {
      instance.post("/product/updateInfo", project).then(result => {
        if (result.code === 0) {
          ElMessage.success(result.data)
        } else {
          ElMessage.error(result.message)
        }

      })

    }
    return {
      baseImageUrl,
      catalogue,
      uploadRef,
      currentTime,
      productForm,
      dialogFormVisible,
      formLabelWidth,
      userStore,
      baseImgUploadUrl,
      deleteGoods,
      handleSuccess,
      handleExceed,
      addGoods,
      updateProduct
    }
  }
}
;
</script>
<style scoped>


div {
  box-sizing: content-box;
}

.img_Product {
  width: 160px;
  height: 160px;
  display: block;
  text-align: center;
  cursor: pointer;
  overflow: hidden;
  padding-bottom: 10px;
  position: relative;
}

el-table-column {
  text-align: center;
}

</style>