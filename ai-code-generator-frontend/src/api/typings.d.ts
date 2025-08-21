declare namespace API {
  type adminGetAppByIdParams = {
    id: number
  }

  type ApiResponseAppVO = {
    code?: number
    message?: string
    data?: AppVO
    timestamp?: string
    requestId?: string
  }

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

  type ApiResponsePageAppVO = {
    code?: number
    message?: string
    data?: PageAppVO
    timestamp?: string
    requestId?: string
  }

  type ApiResponsePageChatHistory = {
    code?: number
    message?: string
    data?: PageChatHistory
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

  type ApiResponseString = {
    code?: number
    message?: string
    data?: string
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

  type AppAddRequest = {
    initPrompt: string
  }

  type AppDeleteRequest = {
    id: number
  }

  type AppQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    id?: number
    userId?: number
    appName?: string
    codeGenType?: string
  }

  type AppUpdateRequest = {
    id: number
    appName?: string
    cover?: string
    priority?: number
  }

  type AppUserUpdateRequest = {
    id: number
    appName?: string
  }

  type AppVO = {
    id?: number
    userId?: number
    appName?: string
    cover?: string
    deployKey?: string
    initPrompt?: string
    priority?: number
    codeGenType?: string
    createTime?: string
    updateTime?: string
    deployedTime?: string
    userVO?: UserVO
  }

  type ChatHistory = {
    id?: number
    message?: string
    messageType?: string
    appId?: number
    userId?: number
    createTime?: string
    updateTime?: string
    isDelete?: number
  }

  type ChatHistoryQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    id?: number
    message?: string
    messageType?: string
    appId?: number
    userId?: number
    lastCreateTime?: string
  }

  type chatToGenerateParams = {
    appId: number
    userMessage: string
  }

  type deployAppParams = {
    appId: number
  }

  type downloadAppCodeParams = {
    appId: number
  }

  type getAppByIdParams = {
    id: number
  }

  type getUserByIdParams = {
    id: number
  }

  type getUserVOByIdParams = {
    id: number
  }

  type listAppChatHistoryParams = {
    appId: number
    pageSize?: number
    lastCreateTime?: string
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

  type PageAppVO = {
    records?: AppVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageChatHistory = {
    records?: ChatHistory[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageUserVO = {
    records?: UserVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type ServerSentEventString = true

  type serveStaticResourceParams = {
    deployKey: string
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
    id: string
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
    id: string
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
