<script id="manage_theadtemplate" type="text/x-jquery-tmpl">
 <br>  <br> <br>  <br>  <br> <br> <br> 

<table id="dashboardtable" class="display" cellspacing="0" width="100%">
       <thead>
           <tr>
           	  <th> Name </th>
           	  <th> Status  </th>
           	  <th> System Create Time </th>
           	  <th> Owner </th>
<th>Instance Id </th>
<th>Architecture </th>
<th>Image Id</th>
<th>Instance Type </th>
<th>Key Name </th>
<th>Private Ip Address</th>
<th>Subnet Id</th>
<th>Vpc Id</th>
<th>cost-center</th>
{{if maxSecurityGroups == 1}}
  <th>Security Group Id 1</th>
  <th>Security Group Name 1</th>
{{else maxSecurityGroups == "2"}}
<th>Security Group Id 1</th>
  <th>Security Group Name 1</th>
<th>Security Group Id 2</th>
  <th>Security Group Name 2</th>
{{else maxSecurityGroups == "3"}}
<th>Security Group Id 1</th>
  <th>Security Group Name 1</th>
<th>Security Group Id 2</th>
  <th>Security Group Name 2</th>
<th>Security Group Id 3</th>
  <th>Security Group Name 3</th>
{{/if}}

           	  <th> Actions </th>
           </tr>
       </thead>
       <tbody>
       </tbody>
       </table>
<br> <br> <br> <br> 
</script>