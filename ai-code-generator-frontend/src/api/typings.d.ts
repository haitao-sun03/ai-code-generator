declare namespace API {
  type ApiResponseBoolean = {
    code?: number
    message?: string
    data?: boolean
    timestamp?: string
    requestId?: string
  }

  type ApiResponseLoginUserVO = {
    code?: number
    message?: string
    data?: LoginUserVO
    timestamp?: string
    requestId?: string
  }

  type ApiResponseLong = {
    code?: number
    message?: string
    data?: number
    timestamp?: string
    requestId?: string
  }

  type ApiResponsePageUserVO = {
    code?: number
    message?: string
    data?: PageUserVO
    timestamp?: string
    requestId?: string
  }

  type ApiResponseUser = {
    code?: number
    message?: string
    data?: User
    timestamp?: string
    requestId?: string
  }

  type ApiResponseUserVO = {
    code?: number
    message?: string
    data?: UserVO
    timestamp?: string
    requestId?: string
  }

  type getUserByIdParams = {
    id: number
  }

  type getUserVOByIdParams = {
    id: number
  }

  type LoginUserVO = {
    id?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    createTime?: string
    updateTime?: string
  }

  type PageUserVO = {
    records?: UserVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type User = {
    id?: number
    userAccount?: string
    userPassword?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    editTime?: string
    createTime?: string
    updateTime?: string
    isDelete?: number
  }

  type UserAddRequest = {
    userName?: string
    userAccount?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserDeleteRequest = {
    id: number
  }

  type UserLoginRequest = {
    userAccount: string
    userPassword: string
  }

  type UserQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    id?: number
    userName?: string
    userAccount?: string
    userProfile?: string
    userRole?: string
  }

  type UserRegisterRequest = {
    userAccount: string
    userPassword: string
    checkPassword: string
  }

  type UserUpdateRequest = {
    id: number
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserVO = {
    id?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    createTime?: string
  }
}
