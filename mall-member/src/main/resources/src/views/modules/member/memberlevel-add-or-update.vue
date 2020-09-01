<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="${column.comments}" prop="name">
      <el-input v-model="dataForm.name" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="growthPoint">
      <el-input v-model="dataForm.growthPoint" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="defaultStatus">
      <el-input v-model="dataForm.defaultStatus" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="freeFreightPoint">
      <el-input v-model="dataForm.freeFreightPoint" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="commentGrowthPoint">
      <el-input v-model="dataForm.commentGrowthPoint" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="priviledgeFreeFreight">
      <el-input v-model="dataForm.priviledgeFreeFreight" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="priviledgeMemberPrice">
      <el-input v-model="dataForm.priviledgeMemberPrice" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="priviledgeBirthday">
      <el-input v-model="dataForm.priviledgeBirthday" placeholder="${column.comments}"></el-input>
    </el-form-item>
    <el-form-item label="${column.comments}" prop="note">
      <el-input v-model="dataForm.note" placeholder="${column.comments}"></el-input>
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
          name: '',
          growthPoint: '',
          defaultStatus: '',
          freeFreightPoint: '',
          commentGrowthPoint: '',
          priviledgeFreeFreight: '',
          priviledgeMemberPrice: '',
          priviledgeBirthday: '',
          note: ''
        },
        dataRule: {
          name: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          growthPoint: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          defaultStatus: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          freeFreightPoint: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          commentGrowthPoint: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          priviledgeFreeFreight: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          priviledgeMemberPrice: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          priviledgeBirthday: [
            { required: true, message: '${column.comments}不能为空', trigger: 'blur' }
          ],
          note: [
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
              url: this.$http.adornUrl(`/member/memberlevel/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.memberLevel.name
                this.dataForm.growthPoint = data.memberLevel.growthPoint
                this.dataForm.defaultStatus = data.memberLevel.defaultStatus
                this.dataForm.freeFreightPoint = data.memberLevel.freeFreightPoint
                this.dataForm.commentGrowthPoint = data.memberLevel.commentGrowthPoint
                this.dataForm.priviledgeFreeFreight = data.memberLevel.priviledgeFreeFreight
                this.dataForm.priviledgeMemberPrice = data.memberLevel.priviledgeMemberPrice
                this.dataForm.priviledgeBirthday = data.memberLevel.priviledgeBirthday
                this.dataForm.note = data.memberLevel.note
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
              url: this.$http.adornUrl(`/member/memberlevel/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'growthPoint': this.dataForm.growthPoint,
                'defaultStatus': this.dataForm.defaultStatus,
                'freeFreightPoint': this.dataForm.freeFreightPoint,
                'commentGrowthPoint': this.dataForm.commentGrowthPoint,
                'priviledgeFreeFreight': this.dataForm.priviledgeFreeFreight,
                'priviledgeMemberPrice': this.dataForm.priviledgeMemberPrice,
                'priviledgeBirthday': this.dataForm.priviledgeBirthday,
                'note': this.dataForm.note
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
