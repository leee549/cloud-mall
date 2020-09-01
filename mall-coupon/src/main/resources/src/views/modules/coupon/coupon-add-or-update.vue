<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="${column.comments}" prop="couponType">
      <el-input v-model="dataForm.couponType" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="couponImg">
      <el-input v-model="dataForm.couponImg" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="couponName">
      <el-input v-model="dataForm.couponName" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="num">
      <el-input v-model="dataForm.num" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="amount">
      <el-input v-model="dataForm.amount" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="perLimit">
      <el-input v-model="dataForm.perLimit" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="minPoint">
      <el-input v-model="dataForm.minPoint" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="startTime">
      <el-input v-model="dataForm.startTime" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="endTime">
      <el-input v-model="dataForm.endTime" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="useType">
      <el-input v-model="dataForm.useType" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="note">
      <el-input v-model="dataForm.note" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="publishCount">
      <el-input v-model="dataForm.publishCount" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="useCount">
      <el-input v-model="dataForm.useCount" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="receiveCount">
      <el-input v-model="dataForm.receiveCount" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="enableStartTime">
      <el-input v-model="dataForm.enableStartTime" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="enableEndTime">
      <el-input v-model="dataForm.enableEndTime" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="code">
      <el-input v-model="dataForm.code" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="memberLevel">
      <el-input v-model="dataForm.memberLevel" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="publish">
      <el-input v-model="dataForm.publish" placeholder="${column.comments}"></el-input>
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
          couponType: '',
          couponImg: '',
          couponName: '',
          num: '',
          amount: '',
          perLimit: '',
          minPoint: '',
          startTime: '',
          endTime: '',
          useType: '',
          note: '',
          publishCount: '',
          useCount: '',
          receiveCount: '',
          enableStartTime: '',
          enableEndTime: '',
          code: '',
          memberLevel: '',
          publish: ''
        },
        dataRule: {
          couponType: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          couponImg: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          couponName: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          num: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          amount: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          perLimit: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          minPoint: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          startTime: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          endTime: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          useType: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          note: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          publishCount: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          useCount: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          receiveCount: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          enableStartTime: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          enableEndTime: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          code: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          memberLevel: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          publish: [
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
              url: this.$http.adornUrl(`/coupon/coupon/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.couponType = data.coupon.couponType
                this.dataForm.couponImg = data.coupon.couponImg
                this.dataForm.couponName = data.coupon.couponName
                this.dataForm.num = data.coupon.num
                this.dataForm.amount = data.coupon.amount
                this.dataForm.perLimit = data.coupon.perLimit
                this.dataForm.minPoint = data.coupon.minPoint
                this.dataForm.startTime = data.coupon.startTime
                this.dataForm.endTime = data.coupon.endTime
                this.dataForm.useType = data.coupon.useType
                this.dataForm.note = data.coupon.note
                this.dataForm.publishCount = data.coupon.publishCount
                this.dataForm.useCount = data.coupon.useCount
                this.dataForm.receiveCount = data.coupon.receiveCount
                this.dataForm.enableStartTime = data.coupon.enableStartTime
                this.dataForm.enableEndTime = data.coupon.enableEndTime
                this.dataForm.code = data.coupon.code
                this.dataForm.memberLevel = data.coupon.memberLevel
                this.dataForm.publish = data.coupon.publish
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
              url: this.$http.adornUrl(`/coupon/coupon/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'couponType': this.dataForm.couponType,
                'couponImg': this.dataForm.couponImg,
                'couponName': this.dataForm.couponName,
                'num': this.dataForm.num,
                'amount': this.dataForm.amount,
                'perLimit': this.dataForm.perLimit,
                'minPoint': this.dataForm.minPoint,
                'startTime': this.dataForm.startTime,
                'endTime': this.dataForm.endTime,
                'useType': this.dataForm.useType,
                'note': this.dataForm.note,
                'publishCount': this.dataForm.publishCount,
                'useCount': this.dataForm.useCount,
                'receiveCount': this.dataForm.receiveCount,
                'enableStartTime': this.dataForm.enableStartTime,
                'enableEndTime': this.dataForm.enableEndTime,
                'code': this.dataForm.code,
                'memberLevel': this.dataForm.memberLevel,
                'publish': this.dataForm.publish
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
