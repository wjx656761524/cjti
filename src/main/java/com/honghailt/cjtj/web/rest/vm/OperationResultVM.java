package com.honghailt.cjtj.web.rest.vm;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 14:00 2019/5/17
 * @Modified By
 */
public class OperationResultVM {

        private Object info;

        private boolean success = true;

        private String msg;

        public OperationResultVM(Object info, boolean success, String msg) {
            this.info = info;
            this.success = success;
            this.msg = msg;
        }

        public Object getInfo() {
            return info;
        }

        public void setInfo(Object info) {
            this.info = info;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "OperationResultVM{" +
                "info=" + info +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                '}';
        }


}
