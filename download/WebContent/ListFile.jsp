<!DOCTYPE html>
<html>
<head>
<title>Select file or older</title>
</head>
<body>
	<table style="width: 70%; font-size: 105%; font-family: Arial; margin-left: 4%; margin-right: 6%; border-collapse: collapse; padding: 4px; border-top: 1px solid #cccccc;">
		<tbody>
		<tr style="height:40px;">
                        <th style="width: 48px; padding: 4px;">&nbsp;</th>
                        <th style="text-align: left;" onmouseover="this.bgColor='#eeeeee';" onmouseout="this.bgColor='#ffffff';">Click on File/Folder" Name
                         &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
                         &emsp;&emsp;Size</th>
                    </tr>
                    <tr onmouseover="this.bgColor='#ffeeff';" onmouseout="this.bgColor='#ffffff';" onclick="window.location='/Softwares (A - F)/Softwares (A - F)/ABBYY FineReader 10.0.102.105?sortby=';">
                        <%= request.getAttribute("listservlet.message") %>
                    </tr>
		</tbody>
	</table>
</body>
</html>