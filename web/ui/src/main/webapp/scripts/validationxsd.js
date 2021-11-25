

				/* POLYFILLS */
				
				/* add .matches if not natively supported */
				if (!Element.prototype.matches)
					Element.prototype.matches = Element.prototype.msMatchesSelector || 
												Element.prototype.webkitMatchesSelector;
												
				/* add .closest if not natively supported */
				if (!Element.prototype.closest)
					Element.prototype.closest = function(s) {
						var el = this;
						do {
							if (el.nodeType !== 1) return null;
							if (el.matches(s)) return el;
							el = el.parentElement || el.parentNode;
						} while (el !== null);
						return null;
					};
				
				/* add .forEach if not natively supported */
				if (!NodeList.prototype.forEach) {
					NodeList.prototype.forEach = function(callback){
						var i = 0;
						while (i != this.length) {
							callback.apply(this, [this[i], i, this]);
							i++;
						}
					};
				}
        
        /* add .previousElementSibling if not supported */
        (function (arr) {
          arr.forEach(function (item) {
            if (item.hasOwnProperty('previousElementSibling')) {
              return;
            }
            Object.defineProperty(item, 'previousElementSibling', {
              configurable: true,
              enumerable: true,
              get: function () {
                let el = this;
                while (el = el.previousSibling) {
                  if (el.nodeType === 1) {
                    return el;
                  }
                }
                return null;
              },
              set: undefined
            });
          });
        })([Element.prototype, CharacterData.prototype]);
			
				/* XML GENERATORS */
				
				var htmlToXML = function(root) {
					var namespaces = [];
				    var prefixes = [];
				    
				    document.querySelectorAll("[data-xsd2html2xml-namespace]:not([data-xsd2html2xml-namespace=''])").forEach(function(o) {
				    	if (namespaces.indexOf(
				    		o.getAttribute("data-xsd2html2xml-namespace")
				    	) == -1) {
					    	namespaces.push(
					    		o.getAttribute("data-xsd2html2xml-namespace")
					    	);
					    	
					    	prefixes.push(
					    		o.getAttribute("data-xsd2html2xml-name").substring(
				    				0, o.getAttribute("data-xsd2html2xml-name").indexOf(":")
				    			)
				    		);
				    	}
				    });
				    
				    var namespaceString = "";
				    
				    namespaces.forEach(function(o,i) {
				    	namespaceString = namespaceString.concat(
				    		"xmlns".concat(
				    			(prefixes[i] == "" ? "=" : ":".concat(prefixes[i].concat("=")))
				    		).concat(
				    			"\"".concat(namespaces[i]).concat("\" ")
				    		)
				    	)
				    });
				    
				    return String.fromCharCode(60).concat("?xml version=\"1.0\"?").concat(String.fromCharCode(62)).concat(getXML(root, false, namespaceString.trim()));
				};
				
				var getXML = function(parent, attributesOnly, namespaceString) {
				    var xml = "";
				    var children = [].slice.call(parent.children);
				    children.forEach(function(o) {
				        if (!o.hasAttribute("hidden")) {
				            switch (o.getAttribute("data-xsd2html2xml-type")) {
				                case "element":
				                    if (!attributesOnly) xml = xml.concat(String.fromCharCode(60)).concat(o.getAttribute("data-xsd2html2xml-name")).concat(getXML(o, true)).concat(String.fromCharCode(62)).concat(function() {
				                        if (o.nodeName.toLowerCase() === "label") {
				                            return getContent(o);
				                        } else return getXML(o)
				                    }()).concat(String.fromCharCode(60)).concat("/").concat(o.getAttribute("data-xsd2html2xml-name")).concat(String.fromCharCode(62));
				                    break;
				                case "attribute":
				                	if (attributesOnly)
										if (getContent(o)
											|| (o.getElementsByTagName("input").length > 0
												? o.getElementsByTagName("input")[0].getAttribute("data-xsd2html2xml-primitive").toLowerCase() === "boolean"
												: false
											))
											xml = xml.concat(" ").concat(o.getAttribute("data-xsd2html2xml-name")).concat("=\"").concat(getContent(o)).concat("\"");
				                    break;
				                case "content":
				                    if (!attributesOnly) xml = xml.concat(getContent(o));
				                    break;
				                default:
				                    if (!attributesOnly) {
				                    	if (!o.getAttribute("data-xsd2html2xml-choice"))
				                    		xml = xml.concat(getXML(o));
				                    		
				                    	if (o.getAttribute("data-xsd2html2xml-choice")) {
				                    		var node = o.previousElementSibling;
				                    		while (node.hasAttribute("data-xsd2html2xml-choice")) {
				                    			node = node.previousElementSibling;
				                    		};
				                    		
				                    		if (node.getElementsByTagName("input")[0].checked)
				                    			xml = xml.concat(getXML(o));
				                    	};
				                    }
				                    break;
				            }
				        }
				    });
				    
				    if (namespaceString) {
				    	xml = xml.substring(0, xml.indexOf(String.fromCharCode(62))).concat(" ").concat(namespaceString).concat(xml.substring(xml.indexOf(String.fromCharCode(62))));
				    }
				    
				    return xml;
				};
				
				var getContent = function(node) {
				    if (node.getElementsByTagName("input").length != 0) {
				        switch (node.getElementsByTagName("input")[0].getAttribute("type").toLowerCase()) {
				            case "checkbox":
				                return node.getElementsByTagName("input")[0].checked;
				            case "file":
				            case "range":
				            case "date":
				            case "time":
				            case "datetime-local":
				            	return node.getElementsByTagName("input")[0].getAttribute("value");
				            default:
				            	switch (node.getElementsByTagName("input")[0].getAttribute("data-xsd2html2xml-primitive").toLowerCase()) {
						            case "gday":
						            case "gmonth":
						            case "gmonthday":
						            case "gyear":
						            case "gyearmonth":
						            	return node.getElementsByTagName("input")[0].getAttribute("value");
						            default:
						            	return node.getElementsByTagName("input")[0].value;
				            	}
				        }
				    } else if (node.getElementsByTagName("select").length != 0) {
				        return node.getElementsByTagName("select")[0].value;
				    } else if (node.getElementsByTagName("textarea").length != 0) {
				    	return node.getElementsByTagName("textarea")[0].value;
				    }
				}
			
				/* HTML POPULATORS */
				
				var addHiddenFields = function() {
					document.querySelectorAll("[data-xsd2html2xml-min], [data-xsd2html2xml-max]").forEach(function(o) {
						//add hidden element
						var newNode = o.previousElementSibling.cloneNode(true);
						
						newNode.setAttribute("hidden", "");
						
						newNode.querySelectorAll("input, textarea, select").forEach(function(p) {
							p.setAttribute("disabled", "");
						});
						
						o.parentElement.insertBefore(
							newNode, o
						);
					});
				};
				
				var ensureMinimum = function() {
					document.querySelectorAll("[data-xsd2html2xml-min], [data-xsd2html2xml-max]").forEach(function(o) {
						//add minimum number of elements
						if (o.hasAttribute("data-xsd2html2xml-min")) {
							//if no minimum, remove element
							if (o.getAttribute("data-xsd2html2xml-min") === "0"
								//check for input elements existing to handle empty elements
								&& o.previousElementSibling.previousElementSibling.querySelector("input, textarea, select")
								//check if element has been populated with data from an xml document
								&& !o.previousElementSibling.previousElementSibling.querySelector("input, textarea, select").hasAttribute("data-xsd2html2xml-filled")) {
								clickRemoveButton(
									o.parentElement.children[0].querySelector("legend > button.remove, span > button.remove")
								);
							//if there is only one allowed element that has been filled, disable the button
							} else if (o.getAttribute("data-xsd2html2xml-max") === "1"
								//check for input elements existing to handle empty elements
								&& o.previousElementSibling.previousElementSibling.querySelector("input, textarea, select")
								//check if element has been populated with data from an xml document
								&& o.previousElementSibling.previousElementSibling.querySelector("input, textarea, select").hasAttribute("data-xsd2html2xml-filled")) {
								o.setAttribute("disabled", "disabled");
							//else, add up to minimum number of elements
							} else {
								var remainder = o.getAttribute("data-xsd2html2xml-min") - (o.parentNode.children.length - 2);
								
								for (i=0; i<remainder; i++) {
									clickAddButton(o);
								};
							};
						};
					});
				};
				
				var xmlToHTML = function(root) {
					var xmlDocument;
					
					//check if form was generated from an XML document 
					if (document.querySelector("meta[name='generator'][content='XSD2HTML2XML v3: https://github.com/MichielCM/xsd2html2xml']").getAttribute("data-xsd2html2xml-source")) {
						//parse xml document from attribute
						xmlDocument = new DOMParser().parseFromString(
							document.querySelector("meta[name='generator'][content='XSD2HTML2XML v3: https://github.com/MichielCM/xsd2html2xml']").getAttribute("data-xsd2html2xml-source"),
							"application/xml"
						);
						
						//start parsing nodes, providing the root node and the corresponding document element
						parseNode(
							xmlDocument.childNodes[0],
							document.querySelector("[data-xsd2html2xml-xpath = '/".concat(xmlDocument.childNodes[0].nodeName).concat("']"))
						);
					};
				};
				
				var setValue = function(element, value) {
					element.querySelector("input, textarea, select").setAttribute("data-xsd2html2xml-filled", "true");
					
					if (element.querySelector("input") !== null) {
						if (element.querySelector("input").getAttribute("data-xsd2html2xml-primitive") === "boolean") {
							if (value === "true") {
								element.querySelector("input").setAttribute("checked", "checked");
							};
						} else {
							element.querySelector("input").setAttribute("value", value);
						};
						
						if (element.querySelector("input").getAttribute("type") === "file") {
							element.querySelector("input").removeAttribute("required");
							//element.querySelector("input").setAttribute("data-xsd2html2xml-required", "true");
						};
					};
					
					if (element.querySelector("textarea") !== null) {
						element.querySelector("textarea").textContent = value;
					};
					
					if (element.querySelector("select") !== null) {
						element.querySelector("select option[value = '".concat(value).concat("']")).setAttribute("selected", "selected");
					};
				};
				
				var parseNode = function(node, element) {
					//iterate through the node's attributes and fill them out
					for (var i=0; i<node.attributes.length; i++) {
						var attribute = element.querySelector(
							"[data-xsd2html2xml-xpath = '".concat(
								element.getAttribute("data-xsd2html2xml-xpath").concat(
									"/@".concat(node.attributes[i].nodeName)
									//"/@*[name() = \"".concat(node.attributes[i].nodeName).concat("\"]")
								)
							).concat("']")
						);
						
						if (attribute !== null) {
							setValue(attribute, node.attributes[i].nodeValue);
						};
					};
					
					//if there is only one (non-element) node, it must contain the value; note: this will not work for potential mixed="true" support
					if (node.childNodes.length === 1 && node.childNodes[0].nodeType === Node.TEXT_NODE) {
						//in the case of complexTypes with simpleContents, select the sub-element that actually contains the input element
						if (element.querySelectorAll("[data-xsd2html2xml-xpath='".concat(element.getAttribute("data-xsd2html2xml-xpath")).concat("']")).length > 0) {
							setValue(element.querySelector("[data-xsd2html2xml-xpath='".concat(element.getAttribute("data-xsd2html2xml-xpath")).concat("']")), node.childNodes[0].nodeValue);
						} else {
							setValue(element, node.childNodes[0].nodeValue);
						};
					//else, iterate through the children
					} else {
						var previousChildName;
						
						for (var i=0; i<node.childNodes.length; i++) {
							var childNode = node.childNodes[i];
							
							if (childNode.nodeType === Node.ELEMENT_NODE) {
								//find the corresponding element
								var childElement = element.querySelector(
									"[data-xsd2html2xml-xpath = '".concat(
										element.getAttribute("data-xsd2html2xml-xpath").concat(
											"/".concat(childNode.nodeName)
											//"/*[name() = \"".concat(childNode.nodeName).concat("\"]")
										)
									).concat("']")
								);
								
								//if there is an add-button (and it is not the first child node being parsed), add an element
								var button;
								
								if (childElement.parentElement.lastElementChild.nodeName.toLowerCase() === "button") {
									button = childElement.parentElement.lastElementChild;
								} else if (childElement.parentElement.parentElement.parentElement.lastElementChild.nodeName.toLowerCase() === "button"
									&& !childElement.parentElement.parentElement.parentElement.lastElementChild.hasAttribute("data-xsd2html2xml-element")) {
									button = childElement.parentElement.parentElement.parentElement.lastElementChild;
								};
								
								if (button !== null && childNode.nodeName === previousChildName) {
									clickAddButton(button);
									
									parseNode(
										childNode,
										button.previousElementSibling.previousElementSibling
										//childElement.parentElement.lastElementChild.previousElementSibling.previousElementSibling
									);
								//else, use the already generated element
								} else {
									parseNode(
										childNode,
										childElement
									);
								};
								
								previousChildName = childNode.nodeName;
							}
						};
					}
				};
			
				/* VALUE FIXERS */
				
				var setValues = function() {
					/* specifically set values on ranges */
					document.querySelectorAll("[type='range']").forEach(function(o) {
						if (o.getAttribute("value")) {
							o.value = o.getAttribute("value").replace(/\D/g, "");
						} else if (o.getAttribute("min")) {
							o.value = o.getAttribute("min");
						} else if (o.getAttribute("max")) {
							o.value = o.getAttribute("max");
						} else {
							o.value = 0;
							o.onchange();
						};
						
						o.previousElementSibling.textContent = o.value;
					});
					
					/* specifically set values on datepickers */
					document.querySelectorAll("[data-xsd2html2xml-primitive='gday']").forEach(function(o) {
						if (o.getAttribute("value")) {
							o.value = o.getAttribute("value").replace(/-+0?/g, "");
						}
					});
					document.querySelectorAll("[data-xsd2html2xml-primitive='gmonth']").forEach(function(o) {
						if (o.getAttribute("value")) {
							o.value = o.getAttribute("value").replace(/-+0?/g, "");
						}
					});
					document.querySelectorAll("[data-xsd2html2xml-primitive='gmonthday']").forEach(function(o) {
						if (o.getAttribute("value")) {
							o.value = new Date().getFullYear().toString().concat(o.getAttribute("value").substring(1));
						}
					});
				};
			
				/* EVENT HANDLERS */
				
				var clickAddButton = function(button) {
					var newNode = button.previousElementSibling.cloneNode(true);
					
					newNode.removeAttribute("hidden");
					
					newNode.querySelectorAll("input, select, textarea").forEach(function(o) {
						if (o.closest("[hidden]") == null)
							o.removeAttribute("disabled");
					});
          
          //set a new random id for radio buttons
          newNode.querySelectorAll("input[type='radio']").forEach(function(o) {
            if (o.parentElement.previousElementSibling != null
              && o.parentElement.previousElementSibling.previousElementSibling != null
              && o.parentElement.previousElementSibling.previousElementSibling.children.length > 0
              && o.parentElement.previousElementSibling.previousElementSibling.children[0].hasAttribute("type")
              && o.parentElement.previousElementSibling.previousElementSibling.children[0].getAttribute("type") === "radio") {
              o.setAttribute("name", o.parentElement.previousElementSibling.previousElementSibling.children[0].getAttribute("name"));
            } else {
              o.setAttribute("name", o.getAttribute("name"));
            };
            
            o.setAttribute("onclick", "clickRadioInput(this, '".concat(o.getAttribute("name")).concat("');"));
          });
					
					button.parentNode.insertBefore(
						newNode, button.previousElementSibling
					);
					
					if ((button.parentNode.children.length - 3) == button.getAttribute("data-xsd2html2xml-max"))
						button.setAttribute("disabled", "disabled");
				}
				
				var clickRemoveButton = function(button) {
					if ((button.closest("section").children.length - 3) == button.closest("section").lastElementChild.previousSibling.getAttribute("data-xsd2html2xml-min"))
						button.closest("section").lastElementChild.previousSibling.click();
					
					if ((button.closest("section").children.length - 3) == button.closest("section").lastElementChild.previousSibling.getAttribute("data-xsd2html2xml-max"))
						button.closest("section").lastElementChild.previousSibling.removeAttribute("disabled");
					if ((button.closest("section").children.length - 3) > -1){
						button.closest("section").removeChild(button.closest("fieldset, label"));
					}
					
				}
				
				var clickRadioInput = function(input, name) {
					var activeSections = [];
					var currentSection = input.parentElement.nextElementSibling;
					
					while (currentSection && currentSection.hasAttribute("data-xsd2html2xml-choice")) {
						activeSections.push(currentSection);
						currentSection = currentSection.nextElementSibling;
					};
					
					document.querySelectorAll("[name=".concat(name).concat("]")).forEach(function(o) {
						o.removeAttribute("checked");
						
						var section = o.parentElement.nextElementSibling;
						
						while (section && section.hasAttribute("data-xsd2html2xml-choice")) {
							section.querySelectorAll("input, select, textarea").forEach(function(p) {
								var contained = false;
								activeSections.forEach(function(q) {
									if (q.contains(p)) contained = true;
								});
								
								if (contained) {
									if (p.closest("[data-xsd2html2xml-choice]") === section) {
										if (p.closest("*[hidden]") === null)
											p.removeAttribute("disabled");
										else
											p.setAttribute("disabled", "disabled");
									}
								} else {
									p.setAttribute("disabled", "disabled");
								};
							});
							
							section = section.nextElementSibling;
						};
					});
					
					input.setAttribute("checked","checked");
				}
				
				var pickFile = function(input, file, type) {
					var resetFilePicker = function(input) {
						input.removeAttribute("value");
						input.removeAttribute("type");
						input.setAttribute("type", "file");
					}
					
					var fileReader = new FileReader();
					
					fileReader.onloadend = function() {
						if (fileReader.error) {
							alert(fileReader.error);
							resetFilePicker(input);
						} else {
							input.setAttribute("value",
								(type.endsWith(":base64binary"))
								? fileReader.result.substring(fileReader.result.indexOf(",") + 1)
								//convert base64 to base16 (hexBinary)
								: atob(fileReader.result.substring(fileReader.result.indexOf(",") + 1))
							    	.split('')
							    	.map(function (aChar) {
							    		return ('0' + aChar.charCodeAt(0).toString(16)).slice(-2);
							    	})
									.join('')
									.toUpperCase()
							);
						};
					};
					
					if(file) {
						fileReader.readAsDataURL(file);
					} else {
						resetFilePicker(input);
					}
					
					if (input.getAttribute("data-xsd2html2xml-required")) input.setAttribute("required", "required");
				}
			
				document.addEventListener("DOMContentLoaded",
					function() {
						/* INITIAL CALLS */
						
						addHiddenFields();
						xmlToHTML(document);
						setValues();
						ensureMinimum();
						
						document.querySelectorAll("[data-xsd2html2xml-filled='true']").forEach(function(o) {
							if (o.closest("[data-xsd2html2xml-choice]")) {
								var node = o.closest("[data-xsd2html2xml-choice]").previousElementSibling;
								while (node) {
									if (!node.hasAttribute("data-xsd2html2xml-choice")) {
										node.querySelector("input[type='radio']").click();
										break;										
									} else {
										node = node.previousElementSibling;
									};
								};
							};
						});
					}
				);
				