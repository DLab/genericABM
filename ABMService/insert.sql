insert into accion (acc_id, acc_descripcion) values (1, 'Consultar');
insert into accion (acc_id, acc_descripcion) values (2, 'Visualizar');
insert into accion (acc_id, acc_descripcion) values (3, 'Editar');
insert into accion (acc_id, acc_descripcion) values (4, 'Eliminar');
insert into accion (acc_id, acc_descripcion) values (5, 'Agregar');
insert into accion (acc_id, acc_descripcion) values (6, 'Simular');

insert into funcion (fnc_id, fnc_orden, fnc_descripcion) values (0, 0, 'Presentación/Dashboard');
insert into funcion (fnc_id, fnc_orden, fnc_descripcion) values (2, 200, 'Administración/Roles');
insert into funcion (fnc_id, fnc_orden, fnc_descripcion) values (3, 300, 'Administración/Usuarios');
insert into funcion (fnc_id, fnc_orden, fnc_descripcion) values (4, 400, 'Procesos/Social Simulations');

insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (0, 1, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (0, 2, '2023-03-05 10:39:03.879');


insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (2, 1, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (2, 2, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (2, 3, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (2, 4, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (2, 5, '2023-03-05 10:39:03.879');

insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (3, 1, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (3, 2, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (3, 3, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (3, 4, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (3, 5, '2023-03-05 10:39:03.879');

insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (4, 1, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (4, 2, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (4, 3, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (4, 4, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (4, 5, '2023-03-05 10:39:03.879');
insert into accionesxfuncion (axf_fnc_id, axf_acc_id, axf_timestamp) values (4, 6, '2023-03-05 10:39:03.879');


insert into rol (rol_id, rol_descripcion) values (1, 'Administrador');
insert into usuario (usr_id, usr_password, usr_nombre, usr_rol_id) values ('Admin', 'Admin', 'Admin', 1);

insert into accionesxfuncion_rol (afr_rol_id, afr_axf_fnc_id, afr_axf_acc_id, afr_timestamp)values (1,2,1,'2023-03-05 11:24:50.448');
insert into accionesxfuncion_rol (afr_rol_id, afr_axf_fnc_id, afr_axf_acc_id, afr_timestamp)values (1,2,3,'2023-03-05 11:24:50.448');
insert into accionesxfuncion_rol (afr_rol_id, afr_axf_fnc_id, afr_axf_acc_id, afr_timestamp)values (1,2,4,'2023-03-05 11:24:50.448');
insert into accionesxfuncion_rol (afr_rol_id, afr_axf_fnc_id, afr_axf_acc_id, afr_timestamp)values (1,2,5,'2023-03-05 11:24:50.448');

insert into simulation_state(id, description) values (1, 'Simulating');
insert into simulation_state(id, description) values (2, 'Simulated');

insert into simulation_type(id, description) values (1, 'Social Simulation');

insert into analysisMethods(name, description, parent_name) values ('Dimensionality reduction', 'Dimensionality reduction', null);
insert into analysisMethods(name, description, parent_name) values ('Clustering', 'Clustering', null);
insert into analysisMethods(name, description, parent_name) values ('Normality analysis', 'Normality analysis', null);
insert into analysisMethods(name, description, parent_name) values ('Linearity analysis', 'Linearity analysis', null);

insert into analysisMethods(name, description, parent_name) values ('Shapiro_Wilk', 'Shapiro-Wilk', 'Normality analysis');
/*insert into analysisMethods(name, description, parent_name) values ('Kolmogorof_Smirnof', 'Kolmogorof-Smirnof', 'Normality analysis');*/
insert into analysisMethods(name, description, parent_name) values ('R2', 'R2', 'Linearity analysis');
insert into analysisMethods(name, description, parent_name, parameters_gui) values ('k_means', 'k-means', 'Clustering'
    , $${"additionalData": [{"name": "k", "type": "number", "decimals": 0, "defaultValue": 3}]}$$);
insert into analysisMethods(name, description, parent_name, parameters_gui) values ('k_median', 'k-median', 'Clustering'
    , $${"additionalData": [{"name": "k", "type": "number", "decimals": 0, "defaultValue": 3}]}$$);
insert into analysisMethods(name, description, parent_name, parameters_gui) values ('k_means/k_median', 'k-means/k-median', 'Clustering'
    , $${"additionalData": [{"name": "k", "type": "number", "decimals": 0, "defaultValue": 3}]}$$);
insert into analysisMethods(name, description, parent_name, parameters_gui) values ('PCA', 'PCA', 'Dimensionality reduction'
    , $${"additionalData": [{"name": "n_components", "type": "number", "decimals": 0, "defaultValue": 2}
    , {"name": "Data type", "type": "list", "options": [{"name": "Mean"}, {"name": "Median"}, {"name": "All Data"}], "defaultValue": "Mean"}]
    }$$);
insert into analysisMethods(name, description, parent_name, parameters_gui) values ('U_map', 'U-map', 'Dimensionality reduction'
    , $${"additionalData": [{"name": "n_neighbors", "type": "number", "decimals": 0, "defaultValue": 30}
    , {"name": "min_dist", "type": "number", "decimals": 2, "defaultValue": 0.3}
    , {"name": "random_state", "type": "number", "decimals": 0, "defaultValue": 3}
    , {"name": "Data type", "type": "list", "options": [{"name": "Mean"}, {"name": "Median"}, {"name": "All Data"}], "defaultValue": "Mean"}]
    }$$);
insert into analysisMethods(name, description, parent_name, parameters_gui) values ('PCA/U_map', 'PCA/U-map', 'Dimensionality reduction'
    , $${"additionalData": [{"name": "n_components", "type": "number", "decimals": 0, "defaultValue": 2}
    , {"name": "n_neighbors", "type": "number", "decimals": 0, "defaultValue": 30}
    , {"name": "min_dist", "type": "number", "decimals": 2, "defaultValue": 0.3}
    , {"name": "random_state", "type": "number", "decimals": 0, "defaultValue": 3}
    , {"name": "Data type", "type": "list", "options": [{"name": "Mean"}, {"name": "Median"}, {"name": "All Data"}], "defaultValue": "Mean"}]
    }$$);


insert into analysisresults_states (id, description) values (1, 'CREATED');
insert into analysisresults_states (id, description) values (2, 'OK');
insert into analysisresults_states (id, description) values (3, 'ERROR');
