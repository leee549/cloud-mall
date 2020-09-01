<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="${column.comments}" prop="skuId">
      <el-input v-model="dataForm.skuId" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="wareId">
      <el-input v-model="dataForm.wareId" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="stock">
      <el-input v-model="dataForm.stock" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="skuName">
      <el-input v-model="dataForm.skuName" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="stockLocked">
      <el-input v-model="dataForm.stockLocked" placeholder="${column.comments}"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          skuId: '',
          wareId: '',
          stock: '',
          skuName: '',
          stockLocked: ''
        },
        dataRule: {
          skuId: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          wareId: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          stock: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          skuName: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          stockLocked: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ware/waresku/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.skuId = data.wareSku.skuId
                this.dataForm.wareId = data.wareSku.wareId
                this.dataForm.stock = data.wareSku.stock
                this.dataForm.skuName = data.wareSku.skuName
                this.dataForm.stockLocked = data.wareSku.stockLocked
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ware/waresku/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'skuId': this.dataForm.skuId,
                'wareId': this.dataForm.wareId,
                'stock': this.dataForm.stock,
                'skuName': this.dataForm.skuName,
                'stockLocked': this.dataForm.stockLocked
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
