<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="${column.comments}" prop="orderReturnId">
      <el-input v-model="dataForm.orderReturnId" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="refund">
      <el-input v-model="dataForm.refund" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="refundSn">
      <el-input v-model="dataForm.refundSn" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="refundStatus">
      <el-input v-model="dataForm.refundStatus" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="refundChannel">
      <el-input v-model="dataForm.refundChannel" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="refundContent">
      <el-input v-model="dataForm.refundContent" placeholder="${column.comments}"></el-input>
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
          orderReturnId: '',
          refund: '',
          refundSn: '',
          refundStatus: '',
          refundChannel: '',
          refundContent: ''
        },
        dataRule: {
          orderReturnId: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          refund: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          refundSn: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          refundStatus: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          refundChannel: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          refundContent: [
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
              url: this.$http.adornUrl(`/order/refundinfo/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.orderReturnId = data.refundInfo.orderReturnId
                this.dataForm.refund = data.refundInfo.refund
                this.dataForm.refundSn = data.refundInfo.refundSn
                this.dataForm.refundStatus = data.refundInfo.refundStatus
                this.dataForm.refundChannel = data.refundInfo.refundChannel
                this.dataForm.refundContent = data.refundInfo.refundContent
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
              url: this.$http.adornUrl(`/order/refundinfo/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'orderReturnId': this.dataForm.orderReturnId,
                'refund': this.dataForm.refund,
                'refundSn': this.dataForm.refundSn,
                'refundStatus': this.dataForm.refundStatus,
                'refundChannel': this.dataForm.refundChannel,
                'refundContent': this.dataForm.refundContent
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
