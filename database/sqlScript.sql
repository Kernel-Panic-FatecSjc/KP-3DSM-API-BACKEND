create database api;
use api;

-- US01 - Usuario
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cargo ENUM('CLT','PJ','PJ/Hora'),
    salario DECIMAL(10,2) NOT NULL,
    tipoContrato VARCHAR(50),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    gerenteId INT NULL,
    dataCriacao TIMESTAMP NOT NULL,

    FOREIGN KEY (gerenteId) REFERENCES usuario(id)
);

-- Projeto
CREATE TABLE projeto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(300),
    status ENUM('EM_PLANEJAMENTO','EM_ANDAMENTO','CONCLUIDO'),
    prazo TIMESTAMP NULL,
    valor_contratado float,
    responsavelId INT,
    dataCriacao TIMESTAMP NOT NULL,

    FOREIGN KEY (responsavelId) REFERENCES usuario(id)
);
-- US02 - Tarefa
CREATE TABLE tarefa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000),
    idProjeto INT NOT NULL,
    idResponsavel INT NOT NULL,
    status ENUM('TO_DO','DOING','DONE','BLOQUEADA'),
    dataCriacao TIMESTAMP NOT NULL,

    FOREIGN KEY (idProjeto) REFERENCES projeto(id),
    FOREIGN KEY (idResponsavel) REFERENCES usuario(id)
);

-- US04 - Apontamento
CREATE TABLE apontamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuarioId INT NOT NULL,
    tarefaId INT NULL,
    tituloSessao VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000),
    tipoAtividade ENUM('ANALISE','DESENVOLVIMENTO','TESTES','CORRECAO_BUG','FEATURE'),
    dataLancamento DATE NOT NULL,
    inicio TIME NOT NULL,
    fim TIME NOT NULL,
    justificativa VARCHAR(1000),
    motivoRejeicao VARCHAR(500),
    estado ENUM('PENDENTE','AGUARDANDO_APROVACAO','APROVADO','REJEITADO'),
    dataCriacao TIMESTAMP NOT NULL,

    FOREIGN KEY (usuarioId) REFERENCES usuario(id),
    FOREIGN KEY (tarefaId) REFERENCES tarefa(id)
);

-- US05 - Bloqueio de Tarefa
CREATE TABLE bloqueio_tarefa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tarefaId INT NOT NULL,
    categoriaImpedimento ENUM('ERRO_DE_ANALISTA','AGUARDANDO_CLIENTE','PROBLEMA_TECNICO','DUVIDA_DE_NEGOCIO','OUTRO'),
    descricaoImpedimento VARCHAR(1000),
    dataInicioBloqueio TIMESTAMP NOT NULL,
    dataFimBloqueio TIMESTAMP NULL,
    tempoBloqueio INT,

    FOREIGN KEY (tarefaId) REFERENCES tarefa(id)
);

-- US07 - Historico de Tarefas
CREATE TABLE historico_tarefa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idTarefa INT NOT NULL,
    idProfissional INT NOT NULL,
    tempoAlocado INT NOT NULL,
    idBloqueio INT NULL,
    categoriaImpedimento ENUM('ERRO_DE_ANALISTA','AGUARDANDO_CLIENTE','PROBLEMA_TECNICO','DUVIDA_DE_NEGOCIO','OUTRO'),
    tempoBloqueio INT,
    dataEvento TIMESTAMP NOT NULL,

    FOREIGN KEY (idTarefa) REFERENCES tarefa(id),
    FOREIGN KEY (idProfissional) REFERENCES usuario(id),
    FOREIGN KEY (idBloqueio) REFERENCES bloqueio_tarefa(id)
);

-- US08 - Auditoria
CREATE TABLE auditoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idLancamento INT NOT NULL,
    idUsuario INT NOT NULL,
    campoAlterado VARCHAR(255) NOT NULL,
    valorAnterior VARCHAR(500) NOT NULL,
    valorNovo VARCHAR(500) NOT NULL,
    dataAlteracao TIMESTAMP NOT NULL,

    FOREIGN KEY (idLancamento) REFERENCES apontamento(id),
    FOREIGN KEY (idUsuario) REFERENCES usuario(id)
);