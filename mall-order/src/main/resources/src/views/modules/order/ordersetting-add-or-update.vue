<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="${column.comments}" prop="flashOrderOvertime">
      <el-input v-model="dataForm.flashOrderOvertime" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="normalOrderOvertime">
      <el-input v-model="dataForm.normalOrderOvertime" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="confirmOvertime">
      <el-input v-model="dataForm.confirmOvertime" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="finishOvertime">
      <el-input v-model="dataForm.finishOvertime" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="commentOvertime">
      <el-input v-model="dataForm.commentOvertime" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="memberLevel">
      <el-input v-model="dataForm.memberLevel" placeholder="${column.comments}"></el-input>
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
          flashOrderOvertime: '',
          normalOrderOvertime: '',
          confirmOvertime: '',
          finishOvertime: '',
          commentOvertime: '',
          memberLevel: ''
        },
        dataRule: {
          flashOrderOvertime: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          normalOrderOvertime: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          confirmOvertime: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          finishOvertime: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          commentOvertime: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          memberLevel: [
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
              url: this.$http.adornUrl(`/order/ordersetting/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.flashOrderOvertime = data.orderSetting.flashOrderOvertime
                this.dataForm.normalOrderOvertime = data.orderSetting.normalOrderOvertime
                this.dataForm.confirmOvertime = data.orderSetting.confirmOvertime
                this.dataForm.finishOvertime = data.orderSetting.finishOvertime
                this.dataForm.commentOvertime = data.orderSetting.commentOvertime
                this.dataForm.memberLevel = data.orderSetting.memberLevel
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
              url: this.$http.adornUrl(`/order/ordersetting/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'flashOrderOvertime': this.dataForm.flashOrderOvertime,
                'normalOrderOvertime': this.dataForm.normalOrderOvertime,
                'confirmOvertime': this.dataForm.confirmOvertime,
                'finishOvertime': this.dataForm.finishOvertime,
                'commentOvertime': this.dataForm.commentOvertime,
                'memberLevel': this.dataForm.memberLevel
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
