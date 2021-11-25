$(function(){
	tree = unescape(tree);
	$("div#jstree").jstree({
		plugins: ["themes","json","grid","dnd","contextmenu"],
		core: {
			data: (tree!="null")?JSON.parse(tree.substring(0,tree.length-1)):"",
			check_callback : true
		},
		grid: {
			columns: [
					{ header: "Nodes",title:"_DATA_"},
									{
										cellClass : "col1",
										value : function(node) {
											return ("<input type='checkbox' "
													+ (node.data != undefined
															&& node.data.GET == 1 ? "checked='checked'"
															: "") + " />");
										},
										header : "GET",
										title : "get",
										valueClass : "spanclass"
									},
									{
										cellClass : "col2",
										value : function(node) {
											return ("<input type='checkbox' "
													+ (node.data != undefined
															&& node.data.POST == 1 ? "checked='checked'"
															: "") + "/>");
										},
										header : "POST",
										title : "post",
										valueClass : "spanclass"
									},
									{
										cellClass : "col3",
										value : function(node) {
											return ("<input type='checkbox' "
													+ (node.data != undefined
															&& node.data.PUT == 1 ? "checked='checked'"
															: "") + "/>");
										},
										header : "PUT",
										title : "put",
										valueClass : "spanclass"
									},
									{
										cellClass : "col4",
										value : function(node) {
											return ("<input type='checkbox' "
													+ (node.data != undefined
															&& node.data.DELETE == 1 ? "checked='checked'"
															: "") + "/>");
										},
										header : "DELETE",
										title : "delete",
										valueClass : "spanclass"
									},
									
									{
										cellClass : "col5",
										value : function(node) {
											return ("<input type='checkbox' "
													+ (node.data != undefined
															&& node.data.TOKEN == 1 ? "checked='checked'"
															: "") + "/>");
										},
										header : "TOKEN",
										title : "token",
										valueClass : "spanclass"
									}],
				resizable:true,
				draggable:true,
				contextmenu:true,
				gridcontextmenu: function (grid,tree,node,val,col,t,target)
				{
					return {
						
					}
				},
				width: 806,
				height: 500
		},
		dnd: {
			drop_finish : function () {
			},
			drag_finish : function () {
			},
			drag_check : function (data) {
				return {
					after : true,
					before : true,
					inside : true
				};
			}
		}
	}).on('loaded.jstree', function() {
		$("div#jstree").jstree('open_all');
		$('input:checkbox').click(function(){
			isSel = true;
		})
		$('#selAll').click( function() {
			if($(this).attr("data-type")=="N"){
				$('input:checkbox').prop("checked", true);
				$(this).attr("data-type","Y")
			}else{
				$('input:checkbox').prop("checked", false)
				$(this).attr("data-type","N")
			}
		})
	}).on("select_cell.jstree-grid",function (e, data, header, node, grid, sourceName ) {
		var node = $("#jstree").jstree(true).get_node(data.node);
		if(node.data==null){
			node.data = {};
			node.data.GET = 0; 
			node.data.POST = 0; 
			node.data.PUT = 0; 
			node.data.DELETE = 0; 
			$('input:checkbox').unbind().click(function(){
				isSel = true;
			})
			isSel = true;
		}
		if(isSel)
			node.data[data.column] = (node.data[data.column]==1)?0:1;
		isSel = 0;
	})
	var isSel = false;
	
	var selectedNode = "";
	
	$('#create').click( function() {
		var sel = $("#jstree").jstree("get_selected");
		if(sel.length==0){
			sel = "#";
		}
		$('#jstree').jstree('create_node', sel, {text : "new Node"}, 'last' , function(new_node){
			$('#jstree').jstree("open_node", sel);
	        var inst = $.jstree.reference(new_node);
	        inst.edit(new_node);
	    });
	})
	
	$('#delete').click( function() {
		$("#jstree").jstree("delete_node", $("#jstree").jstree("get_selected"));
	})
	
	$('#rename').click( function() {
		var ref = $('#jstree').jstree(true);
		var sel = ref.get_selected();
		if (!sel.length) {
			return false;
		}
		sel = sel[0];
		ref.edit(sel);
	})
	
	$('#save').click( function() {
		var v = $('#jstree').jstree(true).get_json('#', -1)
		$.ajax({
			url: "endpoints/save", 
			type:"POST",
			data : JSON.stringify(v),
			success: function(result){
		       
		    }
		});
	})
});
