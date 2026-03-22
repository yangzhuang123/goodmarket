#!/bin/bash

# 批量修改列表页面的main-content类名为list-content
find /Users/young/Desktop/shop/goodmarket/frontend/admin/src/views/modules -name "list.vue" -exec sed -i '' 's/class="main-content"/class="list-content"/g' {} \;