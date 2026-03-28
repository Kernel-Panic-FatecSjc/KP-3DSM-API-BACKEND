CREATE DATABASE IF NOT EXISTS bancogeral;

use bancogeral;

create table usuarios (
	id int auto_increment primary key,
    gerente_id int null,
    nome varchar(100) not null,
    cargo varchar(30) not null,
    email varchar(150) unique not null,
    senha varchar(255) not null,
    data_criacao timestamp default current_timestamp,
    
    constraint fk_usuario_gerente
		foreign key (gerente_id)
        references usuarios(id)
        on delete set null
);

create table projetos (
	id int auto_increment primary key,
    nome varchar(100) not null,
    descricao varchar(300),
    prazo datetime,
    data_criacao timestamp default current_timestamp
);

create table participacao (
	usuario_id int,
    projeto_id int,
    data_atribuicao timestamp default current_timestamp,
    papel varchar(30),
    
    primary key (usuario_id, projeto_id),
    
    constraint fk_part_usuario
		foreign key (usuario_id)
        references usuarios(id)
        on delete cascade,
        
	constraint fk_part_projeto
		foreign key (projeto_id)
        references projetos(id)
        on delete cascade
);

create table tarefas (
	id int auto_increment primary key,
    projeto_id int not null,
    tarefa_pai_id int null,
    usuario_id int null,
    aberta BOOLEAN DEFAULT TRUE,
    
    descricao varchar(500),
    data_criacao timestamp default current_timestamp,
    
    constraint fk_tarefas_tarefas
		foreign key (tarefa_pai_id)
        references tarefas(id)
        ON DELETE CASCADE,
    
    constraint fk_tarefas_projeto
		foreign key (projeto_id)
        references projetos(id)
        ON DELETE CASCADE,
        
    constraint fk_tarefas_usuario
		foreign key (usuario_id)
        references usuarios(id)
        ON DELETE SET NULL
);

create table horas (
	id int auto_increment primary key,
    tarefa_id int null,
    usuario_id int not null,
    titulo_sessao varchar(255) not null,
    tipo_atividade varchar(30) not null,
    descricao text not null,
    data_lancamento timestamp not null,
    inicio time not null,
    fim time not null,
    justificativa text null,
    motivo_rejeicao text null,
    estado varchar(30) default 'PENDENTE',
    data_criacao timestamp default current_timestamp,
    
    constraint fk_horas_tarefa
		foreign key (tarefa_id)
        references tarefas(id)
        ON DELETE SET NULL,
        
    constraint fk_horas_usuario
		foreign key (usuario_id)
        references usuarios(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_tarefas_projeto ON tarefas(projeto_id);
CREATE INDEX idx_tarefas_usuario ON tarefas(usuario_id);
CREATE INDEX idx_horas_usuario_tarefa 
ON horas(usuario_id, tarefa_id);
CREATE INDEX idx_horas_usuario ON horas(usuario_id);